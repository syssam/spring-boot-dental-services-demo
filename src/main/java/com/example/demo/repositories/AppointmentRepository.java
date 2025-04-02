package com.example.demo.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.models.Appointment;
import com.example.demo.models.AppointmentStatus;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    // 查找用户的未来预约（状态为待确认或已确认）
    List<Appointment> findByUserIdAndAppointmentDateGreaterThanEqualAndStatusIn(
            Long userId, LocalDate date, List<AppointmentStatus> statuses);
    
    // 查找用户的过去预约（状态为已完成或未出席）
    @Query("SELECT a FROM Appointment a WHERE a.user.id = :userId AND a.appointmentDate < :date AND a.status IN :statuses ORDER BY a.appointmentDate DESC")
    List<Appointment> findPastAppointmentsByUserId(
            @Param("userId") Long userId, 
            @Param("date") LocalDate date, 
            @Param("statuses") List<AppointmentStatus> statuses);
    
    // 查找用户的已完成预约
    List<Appointment> findByUserIdAndAppointmentDateLessThanEqualAndStatusEquals(
            Long userId, LocalDate date, AppointmentStatus status);
    
    // 查找用户的已取消预约
    List<Appointment> findByUserIdAndStatusEquals(Long userId, AppointmentStatus status);
    
    // 查找牙医在特定诊所和日期的预约
    List<Appointment> findByDentistIdAndClinicIdAndAppointmentDateAndStatusIn(
            Long dentistId, Long clinicId, LocalDate appointmentDate, List<AppointmentStatus> statuses);
}