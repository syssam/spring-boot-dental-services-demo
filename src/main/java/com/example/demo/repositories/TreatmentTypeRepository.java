package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.TreatmentType;

public interface TreatmentTypeRepository extends JpaRepository<TreatmentType, Long> {
    List<TreatmentType> findByActiveTrue();
} 