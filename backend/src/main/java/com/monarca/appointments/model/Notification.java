package com.monarca.appointments.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "appointment_id", nullable = false)
    private Long appointmentId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "type", nullable = false, length = 30)
    private String type; // reservado, confirmado, cancelado, recordatorio

    @Column(name = "channel", nullable = false, length = 30)
    private String channel; // email, web

    @Column(name = "status", nullable = false, length = 30)
    private String status; // enviado, fallido, pendiente

    @Column(name = "title", length = 255, nullable = true)
    private String title;

    @Column(name = "body", columnDefinition = "TEXT", nullable = true)
    private String body;

    @Column(name = "sent_at", nullable = true)
    private LocalDateTime sentAt;

    public Notification() {
    }

    public Notification(Long id, Long appointmentId, Long userId, String type, String channel, 
                       String status, String title, String body, LocalDateTime sentAt) {
        this.id = id;
        this.appointmentId = appointmentId;
        this.userId = userId;
        this.type = type;
        this.channel = channel;
        this.status = status;
        this.title = title;
        this.body = body;
        this.sentAt = sentAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }
}
