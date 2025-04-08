package com.codesignx.dentaldemo.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.codesignx.dentaldemo.models.TreatmentType;
import com.codesignx.dentaldemo.repositories.TreatmentTypeRepository;

@Service
public class TreatmentTypeService {

    private final TreatmentTypeRepository treatmentTypeRepository;
    
    public TreatmentTypeService(TreatmentTypeRepository treatmentTypeRepository) {
        this.treatmentTypeRepository = treatmentTypeRepository;
    }
    
    public List<TreatmentType> findAllActiveTreatmentTypes() {
        return treatmentTypeRepository.findByActiveTrue();
    }
    
    public TreatmentType findById(Long id) {
        return treatmentTypeRepository.findById(id).orElse(null);
    }
    
    public TreatmentType save(TreatmentType treatmentType) {
        return treatmentTypeRepository.save(treatmentType);
    }
} 