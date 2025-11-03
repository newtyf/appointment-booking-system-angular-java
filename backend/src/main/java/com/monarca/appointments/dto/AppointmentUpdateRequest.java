package com.monarca.appointments.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentUpdateRequest {
    
    private Long stylistId;
    private Long serviceId;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;
    
    private String status;
}
