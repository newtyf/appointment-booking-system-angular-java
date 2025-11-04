package com.monarca.appointments.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentCreateRequest {
    
    // Cliente registrado (opcional para walk-in)
    private Long clientId;
    
    // Datos del cliente walk-in (solo si clientId es null)
    private String clientName;
    private String clientPhone;
    private String clientEmail;
    
    // Indica si es walk-in
    private Boolean isWalkIn = false;
    
    @NotNull(message = "Stylist ID is required")
    private Long stylistId;
    
    @NotNull(message = "Service ID is required")
    private Long serviceId;
    
    @NotNull(message = "Appointment date is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;
    
    private String status = "pending";
}
