package com.monarca.appointments.service;

import com.monarca.appointments.model.Appointment;
import com.monarca.appointments.model.Service;
import com.monarca.appointments.model.User;
import com.monarca.appointments.repository.AppointmentRepository;
import com.monarca.appointments.repository.ServiceRepository;
import com.monarca.appointments.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class AvailabilityService {

    private final AppointmentRepository appointmentRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;

    private static final LocalTime BUSINESS_START = LocalTime.of(8, 0);
    private static final LocalTime BUSINESS_END = LocalTime.of(20, 0);
    private static final int SLOT_INTERVAL_MINUTES = 30;

    public AvailabilityService(
            AppointmentRepository appointmentRepository,
            ServiceRepository serviceRepository,
            UserRepository userRepository
    ) {
        this.appointmentRepository = appointmentRepository;
        this.serviceRepository = serviceRepository;
        this.userRepository = userRepository;
    }

    public Map<String, Object> getAvailability(String dateStr, Long serviceId, Long stylistId) {
        // Parse date
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        // Get service duration
        int serviceDuration = 60; // default
        if (serviceId != null) {
            Optional<Service> serviceOpt = serviceRepository.findById(serviceId);
            if (serviceOpt.isPresent()) {
                serviceDuration = serviceOpt.get().getDurationMin();
            }
        }

        // Get stylists
        List<User> stylists;
        if (stylistId != null) {
            Optional<User> stylistOpt = userRepository.findById(stylistId);
            stylists = stylistOpt.map(Collections::singletonList).orElse(Collections.emptyList());
        } else {
            stylists = userRepository.findByRole("stylist");
        }

        // Get existing appointments for the date
        List<Appointment> existingAppointments = appointmentRepository
                .findByDateBetween(startOfDay, endOfDay);

        // Build availability map
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> stylistsAvailability = new ArrayList<>();

        for (User stylist : stylists) {
            Map<String, Object> stylistData = new HashMap<>();
            stylistData.put("stylist_id", stylist.getId());
            stylistData.put("stylist_name", stylist.getName());

            List<String> availableSlots = calculateAvailableSlots(
                    date,
                    stylist.getId(),
                    serviceDuration,
                    existingAppointments
            );

            stylistData.put("available_slots", availableSlots);
            stylistData.put("total_slots", availableSlots.size());

            stylistsAvailability.add(stylistData);
        }

        result.put("date", dateStr);
        result.put("stylists", stylistsAvailability);

        return result;
    }

    private List<String> calculateAvailableSlots(
            LocalDate date,
            Long stylistId,
            int serviceDuration,
            List<Appointment> existingAppointments
    ) {
        List<String> availableSlots = new ArrayList<>();

        // Filter appointments for this stylist
        List<Appointment> stylistAppointments = existingAppointments.stream()
                .filter(apt -> apt.getStylistId().equals(stylistId))
                .filter(apt -> apt.getStatus().equals("pending") || apt.getStatus().equals("confirmed"))
                .toList();

        LocalTime currentTime = BUSINESS_START;

        while (currentTime.isBefore(BUSINESS_END)) {
            LocalDateTime slotStart = LocalDateTime.of(date, currentTime);
            LocalDateTime slotEnd = slotStart.plusMinutes(serviceDuration);

            // Check if slot ends before business hours end
            if (slotEnd.toLocalTime().isAfter(BUSINESS_END)) {
                break;
            }

            // Check if slot conflicts with existing appointments
            boolean isAvailable = stylistAppointments.stream()
                    .noneMatch(apt -> {
                        LocalDateTime aptStart = apt.getDate();
                        LocalDateTime aptEnd = aptStart.plusMinutes(getServiceDuration(apt.getServiceId()));
                        
                        // Check for overlap
                        return (slotStart.isBefore(aptEnd) && slotEnd.isAfter(aptStart));
                    });

            if (isAvailable) {
                availableSlots.add(currentTime.toString());
            }

            currentTime = currentTime.plusMinutes(SLOT_INTERVAL_MINUTES);
        }

        return availableSlots;
    }

    private int getServiceDuration(Long serviceId) {
        return serviceRepository.findById(serviceId)
                .map(Service::getDurationMin)
                .orElse(60);
    }
}
