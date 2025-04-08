package com.codesignx.dentaldemo.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.codesignx.dentaldemo.models.Clinic;
import com.codesignx.dentaldemo.repositories.ClinicRepository;

@Service
public class ClinicService {

    private final ClinicRepository clinicRepository;
    
    public ClinicService(ClinicRepository clinicRepository) {
        this.clinicRepository = clinicRepository;
    }
    
    public List<Clinic> findAllActiveClinics() {
        return clinicRepository.findByActiveTrue();
    }
    
    public Clinic findById(Long id) {
        return clinicRepository.findById(id).orElse(null);
    }
    
    public Clinic save(Clinic clinic) {
        return clinicRepository.save(clinic);
    }
} 