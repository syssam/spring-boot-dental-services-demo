package com.example.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repositories.ServiceRepository;

@Service
public class DentalServiceService {
    @Autowired  
    private ServiceRepository serviceRepository;
    
    public DentalServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public List<com.example.demo.models.Service> findAllActiveServices() {
        return serviceRepository.findByActiveTrue();
    }
}