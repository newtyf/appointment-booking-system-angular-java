package com.monarca.appointments.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Cliente registrado (opcional - NULL si es walk-in)
    @Column(name = "client_id", nullable = true)
    private Long clientId;

    // Datos del cliente walk-in (solo si client_id es NULL)
    @Column(name = "client_name", length = 100, nullable = true)
    private String clientName;

    @Column(name = "client_phone", length = 20, nullable = true)
    private String clientPhone;

    @Column(name = "client_email", length = 50, nullable = true)
    private String clientEmail;

    // Indica si es un cliente walk-in (sin cuenta) o registrado
    @Column(name = "is_walk_in", nullable = false)
    private Boolean isWalkIn = false;

    // Campos principales
    @Column(name = "stylist_id", nullable = false)
    private Long stylistId;

    @Column(name = "service_id", nullable = false)
    private Long serviceId;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "status", nullable = false, length = 30)
    private String status = "pending";

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "modified_by", nullable = false)
    private Long modifiedBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
