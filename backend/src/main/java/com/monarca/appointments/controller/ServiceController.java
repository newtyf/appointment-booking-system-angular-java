package com.monarca.appointments.controller;

import com.monarca.appointments.model.Service;
import com.monarca.appointments.service.ServiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/services")
public class ServiceController {

    private final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping
    public ResponseEntity<List<Service>> getAllServices() {
        return ResponseEntity.ok(serviceService.getAllServices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Service> getServiceById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceService.getServiceById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Service> createService(@RequestBody Service service) {
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceService.createService(service));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Service> updateService(@PathVariable Long id, @RequestBody Service service) {
        return ResponseEntity.ok(serviceService.updateService(id, service));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }
}
