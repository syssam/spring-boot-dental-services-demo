package com.codesignx.dentaldemo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codesignx.dentaldemo.models.Clinic;

public interface ClinicRepository extends JpaRepository<Clinic, Long> {
    List<Clinic> findByActiveTrue();
} 