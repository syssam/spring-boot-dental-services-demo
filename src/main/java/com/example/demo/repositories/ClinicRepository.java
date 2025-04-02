package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.Clinic;

public interface ClinicRepository extends JpaRepository<Clinic, Long> {
    List<Clinic> findByActiveTrue();
} 