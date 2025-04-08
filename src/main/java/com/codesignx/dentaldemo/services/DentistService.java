package com.codesignx.dentaldemo.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.codesignx.dentaldemo.models.Dentist;
import com.codesignx.dentaldemo.repositories.DentistRepository;

@Service
public class DentistService {

    private final DentistRepository dentistRepository;
    
    public DentistService(DentistRepository dentistRepository) {
        this.dentistRepository = dentistRepository;
    }
    
    public List<Dentist> findAllActiveDentists() {
        return dentistRepository.findByActiveTrue();
    }
    
    public List<Dentist> findAvailableDentistsByClinicAndDate(Long clinicId, LocalDate date) {
        // 根据诊所和日期查找可用牙医
        return dentistRepository.findDentistsByClinicAndDate(clinicId, date);
    }
    
    public List<Dentist> findDentistsByClinic(Long clinicId) {
        return dentistRepository.findActiveDentistsByClinic(clinicId);
    }
    
    public Dentist findById(Long id) {
        return dentistRepository.findById(id).orElse(null);
    }
    
    public Dentist save(Dentist dentist) {
        return dentistRepository.save(dentist);
    }
} 