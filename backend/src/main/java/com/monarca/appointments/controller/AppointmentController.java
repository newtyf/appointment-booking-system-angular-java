package com.monarca.appointments.controller;

import com.monarca.appointments.model.Appointment;
import com.monarca.appointments.model.User;
import com.monarca.appointments.service.AppointmentService;
import com.monarca.appointments.service.AvailabilityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final AvailabilityService availabilityService;

    public AppointmentController(AppointmentService appointmentService, AvailabilityService availabilityService) {
        this.appointmentService = appointmentService;
        this.availabilityService = availabilityService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<List<Appointment>> listAllAppointments() {
        return ResponseEntity.ok(appointmentService.listAllAppointments());
    }

    @GetMapping("/my-appointments")
    public ResponseEntity<List<Appointment>> listMyAppointments(@AuthenticationPrincipal User currentUser) {
        List<Appointment> appointments;
        
        switch (currentUser.getRole()) {
            case "admin", "receptionist" -> appointments = appointmentService.listAllAppointments();
            case "stylist" -> appointments = appointmentService.getAppointmentsByStylist(currentUser.getId());
            case "client" -> appointments = appointmentService.getAppointmentsByClient(currentUser.getId());
            default -> {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/availability")
    public ResponseEntity<Map<String, Object>> getAvailability(
            @RequestParam String date,
            @RequestParam(required = false) Long serviceId,
            @RequestParam(required = false) Long stylistId
    ) {
        Map<String, Object> availability = availabilityService.getAvailability(date, serviceId, stylistId);
        return ResponseEntity.ok(availability);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointment(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser
    ) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        
        // Check authorization
        if (!canUserAccessAppointment(appointment, currentUser)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        return ResponseEntity.ok(appointment);
    }

    @PostMapping("/")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<Appointment> createAppointment(
            @RequestBody Appointment appointment,
            @AuthenticationPrincipal User currentUser
    ) {
        Appointment created = appointmentService.createAppointment(appointment, currentUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/book")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Appointment> bookAppointment(
            @RequestBody Appointment appointment,
            @AuthenticationPrincipal User currentUser
    ) {
        // Set client ID from current user
        appointment.setClientId(currentUser.getId());
        appointment.setIsWalkIn(false);
        
        Appointment created = appointmentService.createAppointment(appointment, currentUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/walk-in")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<Appointment> createWalkInAppointment(
            @RequestBody Appointment appointment,
            @AuthenticationPrincipal User currentUser
    ) {
        // Ensure walk-in fields are set
        appointment.setIsWalkIn(true);
        appointment.setClientId(null);
        
        Appointment created = appointmentService.createAppointment(appointment, currentUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<Appointment> updateAppointment(
            @PathVariable Long id,
            @RequestBody Appointment appointment,
            @AuthenticationPrincipal User currentUser
    ) {
        Appointment updated = appointmentService.updateAppointment(id, appointment, currentUser.getId());
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Appointment> updateAppointmentStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @AuthenticationPrincipal User currentUser
    ) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        
        // Authorization checks
        if (currentUser.getRole().equals("stylist") && !appointment.getStylistId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        if (currentUser.getRole().equals("client")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        appointment.setStatus(status);
        Appointment updated = appointmentService.updateAppointment(id, appointment, currentUser.getId());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser
    ) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        
        // Clients can only cancel their own appointments
        if (currentUser.getRole().equals("client") && !appointment.getClientId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        // Stylists cannot delete appointments
        if (currentUser.getRole().equals("stylist")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }

    private boolean canUserAccessAppointment(Appointment appointment, User user) {
        return switch (user.getRole()) {
            case "admin", "receptionist" -> true;
            case "stylist" -> appointment.getStylistId().equals(user.getId());
            case "client" -> appointment.getClientId() != null && appointment.getClientId().equals(user.getId());
            default -> false;
        };
    }
}

