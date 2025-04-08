package com.codesignx.dentaldemo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.codesignx.dentaldemo.models.Service;

public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findByActiveTrue();
}
