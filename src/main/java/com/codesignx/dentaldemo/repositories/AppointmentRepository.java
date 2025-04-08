package com.codesignx.dentaldemo.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codesignx.dentaldemo.models.Appointment;
import com.codesignx.dentaldemo.models.AppointmentStatus;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    // Find user's future appointments (status: pending or confirmed)
    List<Appointment> findByUserIdAndAppointmentDateGreaterThanEqualAndStatusIn(
            Long userId, LocalDate date, List<AppointmentStatus> statuses);
    
    // Find user's past appointments (status: completed or no-show)
    @Query("SELECT a FROM Appointment a WHERE a.user.id = :userId AND a.appointmentDate < :date AND a.status IN :statuses ORDER BY a.appointmentDate DESC")
    List<Appointment> findPastAppointmentsByUserId(
            @Param("userId") Long userId, 
            @Param("date") LocalDate date, 
            @Param("statuses") List<AppointmentStatus> statuses);
    
    // Find user's completed appointments
    List<Appointment> findByUserIdAndAppointmentDateLessThanEqualAndStatusEquals(
            Long userId, LocalDate date, AppointmentStatus status);
    
    // Find user's cancelled appointments
    List<Appointment> findByUserIdAndStatusEquals(Long userId, AppointmentStatus status);
    
    // Find dentist's appointments for specific clinic and date
    List<Appointment> findByDentistIdAndClinicIdAndAppointmentDateAndStatusIn(
            Long dentistId, Long clinicId, LocalDate appointmentDate, List<AppointmentStatus> statuses);
}