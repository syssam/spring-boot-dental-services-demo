package com.codesignx.dentaldemo.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codesignx.dentaldemo.models.Dentist;
import com.codesignx.dentaldemo.models.DentistSchedule.DayOfWeek;

public interface DentistRepository extends JpaRepository<Dentist, Long> {
    List<Dentist> findByActiveTrue();
    
    @Query("SELECT DISTINCT d FROM Dentist d JOIN d.schedules ds " +
           "WHERE d.active = true " +
           "AND ds.clinic.id = :clinicId " +
           "AND ds.active = true " +
           "AND :date BETWEEN ds.effectiveFrom AND COALESCE(ds.effectiveTo, :date) " +
           "AND ds.dayOfWeek = :dayOfWeek")
    List<Dentist> findDentistsByClinicAndDate(@Param("clinicId") Long clinicId, 
                                             @Param("date") LocalDate date,
                                             @Param("dayOfWeek") DayOfWeek dayOfWeek);
    
    default List<Dentist> findDentistsByClinicAndDate(Long clinicId, LocalDate date) {
        // 从LocalDate获取DayOfWeek，并转换为我们的枚举
        java.time.DayOfWeek javaDayOfWeek = date.getDayOfWeek();
        DayOfWeek dayOfWeek = DayOfWeek.valueOf(javaDayOfWeek.name());
        return findDentistsByClinicAndDate(clinicId, date, dayOfWeek);
    }
    
    default List<Dentist> findActiveDentistsByClinic(Long clinicId) {
        return findByActiveTrue();
    }
} 