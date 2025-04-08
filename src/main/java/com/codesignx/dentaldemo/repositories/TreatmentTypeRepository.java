package com.codesignx.dentaldemo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codesignx.dentaldemo.models.TreatmentType;

public interface TreatmentTypeRepository extends JpaRepository<TreatmentType, Long> {
    List<TreatmentType> findByActiveTrue();
} 