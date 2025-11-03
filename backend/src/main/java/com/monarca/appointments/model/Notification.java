package com.monarca.appointments.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
