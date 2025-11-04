package com.monarca.appointments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {
    private Long id;
    private Long clientId;
    private String clientName;
    private String clientPhone;
    private String clientEmail;
    private Boolean isWalkIn;
    private Long stylistId;
    private Long serviceId;
    private LocalDateTime date;
    private String status;
    private Long createdBy;
    private Long modifiedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
