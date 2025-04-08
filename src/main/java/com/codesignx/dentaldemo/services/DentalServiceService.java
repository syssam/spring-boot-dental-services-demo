package com.codesignx.dentaldemo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codesignx.dentaldemo.repositories.ServiceRepository;

@Service
public class DentalServiceService {
    @Autowired  
    private ServiceRepository serviceRepository;
    
    public DentalServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public List<com.codesignx.dentaldemo.models.Service> findAllActiveServices() {
        return serviceRepository.findByActiveTrue();
    }
}