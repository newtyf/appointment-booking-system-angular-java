package com.monarca.appointments.service;

import com.monarca.appointments.exception.BadRequestException;
import com.monarca.appointments.exception.ResourceNotFoundException;
import com.monarca.appointments.model.Appointment;
import com.monarca.appointments.model.Service;
import com.monarca.appointments.repository.AppointmentRepository;
import com.monarca.appointments.repository.ServiceRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final ServiceRepository serviceRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, ServiceRepository serviceRepository) {
        this.appointmentRepository = appointmentRepository;
        this.serviceRepository = serviceRepository;
    }

    private void validateDateTime(LocalDateTime appointmentDate) {
        LocalDateTime now = LocalDateTime.now();
        
        // No se pueden agendar citas en el pasado
        if (appointmentDate.isBefore(now)) {
            throw new BadRequestException("No se pueden agendar citas en el pasado");
        }
        
        // Validar horario de trabajo (8 AM - 8 PM)
        int hour = appointmentDate.getHour();
        if (hour < 8 || hour >= 20) {
            throw new BadRequestException("Las citas deben ser entre las 8:00 AM y 8:00 PM");
        }
    }

    private void checkStylistAvailability(Long stylistId, LocalDateTime date, Long serviceId, Long excludeAppointmentId) {
        // Obtener duración del servicio
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado"));
        
        // Calcular rango de tiempo de la nueva cita
        LocalDateTime endTime = date.plusMinutes(service.getDurationMin());
        
        // Buscar citas que se traslapen
        List<Appointment> conflictingAppointments = appointmentRepository.findConflictingAppointments(
                stylistId, date, endTime
        );
        
        // Excluir la cita actual si se está actualizando
        if (excludeAppointmentId != null) {
            conflictingAppointments = conflictingAppointments.stream()
                    .filter(a -> !a.getId().equals(excludeAppointmentId))
                    .collect(Collectors.toList());
        }
        
        if (!conflictingAppointments.isEmpty()) {
            throw new BadRequestException("El estilista no está disponible en ese horario");
        }
    }

    public List<Appointment> listAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));
    }

    public List<Appointment> getAppointmentsByClient(Long clientId) {
        return appointmentRepository.findByClientId(clientId);
    }

    public List<Appointment> getAppointmentsByStylist(Long stylistId) {
        return appointmentRepository.findByStylistId(stylistId);
    }

    @Transactional
    public Appointment createAppointment(Appointment appointment, Long createdBy) {
        // Validar datos de cliente
        if (appointment.getIsWalkIn()) {
            if (appointment.getClientName() == null || appointment.getClientPhone() == null) {
                throw new BadRequestException("Se requiere nombre y teléfono para clientes walk-in");
            }
        } else {
            if (appointment.getClientId() == null) {
                throw new BadRequestException("Se requiere ID de cliente para citas registradas");
            }
        }

        // Validar fecha y hora
        validateDateTime(appointment.getDate());

        // Verificar disponibilidad del estilista
        checkStylistAvailability(appointment.getStylistId(), appointment.getDate(), appointment.getServiceId(), null);

        // Establecer campos de auditoría
        if (appointment.getStatus() == null) {
            appointment.setStatus("pending");
        }
        appointment.setCreatedBy(createdBy);
        appointment.setModifiedBy(createdBy);

        return appointmentRepository.save(appointment);
    }

    @Transactional
    public Appointment updateAppointment(Long id, Appointment updatedAppointment, Long modifiedBy) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));

        if (updatedAppointment.getDate() != null) {
            validateDateTime(updatedAppointment.getDate());
            
            Long serviceId = updatedAppointment.getServiceId() != null ? updatedAppointment.getServiceId() : appointment.getServiceId();
            Long stylistId = updatedAppointment.getStylistId() != null ? updatedAppointment.getStylistId() : appointment.getStylistId();
            
            checkStylistAvailability(stylistId, updatedAppointment.getDate(), serviceId, id);
            appointment.setDate(updatedAppointment.getDate());
        }

        if (updatedAppointment.getStylistId() != null) {
            LocalDateTime date = updatedAppointment.getDate() != null ? updatedAppointment.getDate() : appointment.getDate();
            Long serviceId = updatedAppointment.getServiceId() != null ? updatedAppointment.getServiceId() : appointment.getServiceId();
            
            checkStylistAvailability(updatedAppointment.getStylistId(), date, serviceId, id);
            appointment.setStylistId(updatedAppointment.getStylistId());
        }

        if (updatedAppointment.getServiceId() != null) {
            appointment.setServiceId(updatedAppointment.getServiceId());
        }

        if (updatedAppointment.getStatus() != null) {
            appointment.setStatus(updatedAppointment.getStatus());
        }

        appointment.setModifiedBy(modifiedBy);
        return appointmentRepository.save(appointment);
    }

    @Transactional
    public void deleteAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cita no encontrada");
        }
        appointmentRepository.deleteById(id);
    }
}
