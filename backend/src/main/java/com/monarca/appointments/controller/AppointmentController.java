package com.monarca.appointments.controller;

import com.monarca.appointments.dto.AppointmentCreateRequest;
import com.monarca.appointments.dto.AppointmentResponse;
import com.monarca.appointments.dto.AppointmentUpdateRequest;
import com.monarca.appointments.service.AppointmentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<List<AppointmentResponse>> listAllAppointments() {
        return ResponseEntity.ok(appointmentService.listAllAppointments());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'STYLIST', 'CLIENT')")
    public ResponseEntity<AppointmentResponse> getAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'CLIENT')")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByClient(clientId));
    }

    @GetMapping("/stylist/{stylistId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'STYLIST')")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByStylist(@PathVariable Long stylistId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByStylist(stylistId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'CLIENT')")
    public ResponseEntity<AppointmentResponse> createAppointment(
            @Valid @RequestBody AppointmentCreateRequest request,
            HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        AppointmentResponse response = appointmentService.createAppointment(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<AppointmentResponse> updateAppointment(
            @PathVariable Long id,
            @Valid @RequestBody AppointmentUpdateRequest request,
            HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        return ResponseEntity.ok(appointmentService.updateAppointment(id, request, userId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }
}
