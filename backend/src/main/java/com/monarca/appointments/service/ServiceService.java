package com.monarca.appointments.service;

import com.monarca.appointments.exception.ResourceNotFoundException;
import com.monarca.appointments.model.Service;
import com.monarca.appointments.repository.ServiceRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@org.springframework.stereotype.Service
public class ServiceService {

    private final ServiceRepository serviceRepository;

    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }

    public Service getServiceById(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado"));
    }

    @Transactional
    public Service createService(Service service) {
        return serviceRepository.save(service);
    }

    @Transactional
    public Service updateService(Long id, Service serviceDetails) {
        Service service = getServiceById(id);
        
        if (serviceDetails.getName() != null) {
            service.setName(serviceDetails.getName());
        }
        if (serviceDetails.getDescription() != null) {
            service.setDescription(serviceDetails.getDescription());
        }
        if (serviceDetails.getDurationMin() != null) {
            service.setDurationMin(serviceDetails.getDurationMin());
        }
        if (serviceDetails.getPrice() != null) {
            service.setPrice(serviceDetails.getPrice());
        }
        
        return serviceRepository.save(service);
    }

    @Transactional
    public void deleteService(Long id) {
        if (!serviceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Servicio no encontrado");
        }
        serviceRepository.deleteById(id);
    }
}
