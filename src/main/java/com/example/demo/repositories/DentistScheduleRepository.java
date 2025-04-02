package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.DentistSchedule;
import com.example.demo.models.DentistSchedule.DayOfWeek;

public interface DentistScheduleRepository extends JpaRepository<DentistSchedule, Long> {
    // 查找牙医在特定诊所和工作日的时间表
    List<DentistSchedule> findByDentistIdAndClinicIdAndDayOfWeekAndActiveTrue(
            Long dentistId, Long clinicId, DayOfWeek dayOfWeek);
}