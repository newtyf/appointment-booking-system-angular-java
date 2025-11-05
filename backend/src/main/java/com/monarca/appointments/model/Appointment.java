package com.monarca.appointments.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
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

    public Appointment() {
    }

    public Appointment(Long id, Long clientId, String clientName, String clientPhone, String clientEmail, 
                      Boolean isWalkIn, Long stylistId, Long serviceId, LocalDateTime date, String status, 
                      Long createdBy, Long modifiedBy, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.clientId = clientId;
        this.clientName = clientName;
        this.clientPhone = clientPhone;
        this.clientEmail = clientEmail;
        this.isWalkIn = isWalkIn;
        this.stylistId = stylistId;
        this.serviceId = serviceId;
        this.date = date;
        this.status = status;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public Boolean getIsWalkIn() {
        return isWalkIn;
    }

    public void setIsWalkIn(Boolean isWalkIn) {
        this.isWalkIn = isWalkIn;
    }

    public Long getStylistId() {
        return stylistId;
    }

    public void setStylistId(Long stylistId) {
        this.stylistId = stylistId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
