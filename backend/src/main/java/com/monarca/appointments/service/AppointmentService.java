package com.monarca.appointments.service;

import com.monarca.appointments.dto.AppointmentCreateRequest;
import com.monarca.appointments.dto.AppointmentResponse;
import com.monarca.appointments.dto.AppointmentUpdateRequest;
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

    public List<AppointmentResponse> listAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public AppointmentResponse getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));
        return toResponse(appointment);
    }

    public List<AppointmentResponse> getAppointmentsByClient(Long clientId) {
        return appointmentRepository.findByClientId(clientId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<AppointmentResponse> getAppointmentsByStylist(Long stylistId) {
        return appointmentRepository.findByStylistId(stylistId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public AppointmentResponse createAppointment(AppointmentCreateRequest request, Long createdBy) {
        // Validar datos de cliente
        if (request.getIsWalkIn()) {
            if (request.getClientName() == null || request.getClientPhone() == null) {
                throw new BadRequestException("Se requiere nombre y teléfono para clientes walk-in");
            }
        } else {
            if (request.getClientId() == null) {
                throw new BadRequestException("Se requiere ID de cliente para citas registradas");
            }
        }

        // Validar fecha y hora
        validateDateTime(request.getDate());

        // Verificar disponibilidad del estilista
        checkStylistAvailability(request.getStylistId(), request.getDate(), request.getServiceId(), null);

        // Crear cita
        Appointment appointment = new Appointment();
        appointment.setClientId(request.getClientId());
        appointment.setClientName(request.getClientName());
        appointment.setClientPhone(request.getClientPhone());
        appointment.setClientEmail(request.getClientEmail());
        appointment.setIsWalkIn(request.getIsWalkIn());
        appointment.setStylistId(request.getStylistId());
        appointment.setServiceId(request.getServiceId());
        appointment.setDate(request.getDate());
        appointment.setStatus(request.getStatus() != null ? request.getStatus() : "pending");
        appointment.setCreatedBy(createdBy);
        appointment.setModifiedBy(createdBy);

        appointment = appointmentRepository.save(appointment);
        return toResponse(appointment);
    }

    @Transactional
    public AppointmentResponse updateAppointment(Long id, AppointmentUpdateRequest request, Long modifiedBy) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));

        if (request.getDate() != null) {
            validateDateTime(request.getDate());
            
            Long serviceId = request.getServiceId() != null ? request.getServiceId() : appointment.getServiceId();
            Long stylistId = request.getStylistId() != null ? request.getStylistId() : appointment.getStylistId();
            
            checkStylistAvailability(stylistId, request.getDate(), serviceId, id);
            appointment.setDate(request.getDate());
        }

        if (request.getStylistId() != null) {
            LocalDateTime date = request.getDate() != null ? request.getDate() : appointment.getDate();
            Long serviceId = request.getServiceId() != null ? request.getServiceId() : appointment.getServiceId();
            
            checkStylistAvailability(request.getStylistId(), date, serviceId, id);
            appointment.setStylistId(request.getStylistId());
        }

        if (request.getServiceId() != null) {
            appointment.setServiceId(request.getServiceId());
        }

        if (request.getStatus() != null) {
            appointment.setStatus(request.getStatus());
        }

        appointment.setModifiedBy(modifiedBy);
        appointment = appointmentRepository.save(appointment);
        
        return toResponse(appointment);
    }

    @Transactional
    public void deleteAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cita no encontrada");
        }
        appointmentRepository.deleteById(id);
    }

    private AppointmentResponse toResponse(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getId(),
                appointment.getClientId(),
                appointment.getClientName(),
                appointment.getClientPhone(),
                appointment.getClientEmail(),
                appointment.getIsWalkIn(),
                appointment.getStylistId(),
                appointment.getServiceId(),
                appointment.getDate(),
                appointment.getStatus(),
                appointment.getCreatedBy(),
                appointment.getModifiedBy(),
                appointment.getCreatedAt(),
                appointment.getUpdatedAt()
        );
    }
}
