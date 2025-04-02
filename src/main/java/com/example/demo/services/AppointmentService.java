package com.example.demo.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.AppointmentDto;
import com.example.demo.models.Appointment;
import com.example.demo.models.AppointmentStatus;
import com.example.demo.models.Clinic;
import com.example.demo.models.Dentist;
import com.example.demo.models.DentistSchedule;
import com.example.demo.models.TreatmentType;
import com.example.demo.models.User;
import com.example.demo.repositories.AppointmentRepository;
import com.example.demo.repositories.DentistScheduleRepository;
import com.example.demo.models.DentistSchedule.DayOfWeek;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DentistService dentistService;
    private final ClinicService clinicService;
    private final TreatmentTypeService treatmentTypeService;
    private final UserService userService;
    private final DentistScheduleRepository dentistScheduleRepository;
    
    @Autowired
    public AppointmentService(
            AppointmentRepository appointmentRepository,
            DentistService dentistService,
            ClinicService clinicService,
            TreatmentTypeService treatmentTypeService,
            UserService userService,
            DentistScheduleRepository dentistScheduleRepository) {
        this.appointmentRepository = appointmentRepository;
        this.dentistService = dentistService;
        this.clinicService = clinicService;
        this.treatmentTypeService = treatmentTypeService;
        this.userService = userService;
        this.dentistScheduleRepository = dentistScheduleRepository;
    }
    
    public List<Appointment> findUpcomingAppointmentsByUserId(Long userId) {
        LocalDate today = LocalDate.now();
        return appointmentRepository.findByUserIdAndAppointmentDateGreaterThanEqualAndStatusIn(
                userId, today, List.of(AppointmentStatus.PENDING, AppointmentStatus.CONFIRMED));
    }
    
    public List<Appointment> findPastAppointmentsByUserId(Long userId) {
        LocalDate today = LocalDate.now();
        // 只获取已完成和未出席的预约，且日期必须是过去的日期
        return appointmentRepository.findPastAppointmentsByUserId(
                userId, today, List.of(AppointmentStatus.COMPLETED, AppointmentStatus.NO_SHOW));
    }
    
    public List<Appointment> findCompletedAppointmentsByUserId(Long userId) {
        LocalDate today = LocalDate.now();
        return appointmentRepository.findByUserIdAndAppointmentDateLessThanEqualAndStatusEquals(
                userId, today, AppointmentStatus.COMPLETED);
    }
    
    public List<Appointment> findCancelledAppointmentsByUserId(Long userId) {
        return appointmentRepository.findByUserIdAndStatusEquals(userId, AppointmentStatus.CANCELLED);
    }
    
    public Appointment findById(Long id) {
        return appointmentRepository.findById(id).orElse(null);
    }
    
    public List<String> findAvailableTimeSlots(Long dentistId, Long clinicId, LocalDate date, Long treatmentId) {
        // 获取牙医、诊所和治疗类型信息
        Dentist dentist = dentistService.findById(dentistId);
        Clinic clinic = clinicService.findById(clinicId);
        TreatmentType treatment = treatmentTypeService.findById(treatmentId);
        
        if (dentist == null || clinic == null || treatment == null) {
            return new ArrayList<>();
        }
        
        // 获取牙医在该诊所和日期的工作时间表
        java.time.DayOfWeek javaDayOfWeek = date.getDayOfWeek();
        DayOfWeek dayOfWeek = DayOfWeek.valueOf(javaDayOfWeek.name());
        
        System.out.println("查询牙医ID: " + dentistId + " 诊所ID: " + clinicId + " 日期: " + date + " 星期: " + dayOfWeek);
        List<DentistSchedule> schedules = dentistScheduleRepository.findByDentistIdAndClinicIdAndDayOfWeekAndActiveTrue(
                dentistId, clinicId, dayOfWeek);
        
        System.out.println("找到工作时段数量: " + schedules.size());
        if (schedules.isEmpty()) {
            System.out.println("警告: 没有找到该牙医在该诊所的工作时段!");
            return new ArrayList<>();
        }
        
        // 获取牙医在该日期已有的预约
        List<Appointment> existingAppointments = appointmentRepository.findByDentistIdAndClinicIdAndAppointmentDateAndStatusIn(
                dentistId, clinicId, date, List.of(AppointmentStatus.PENDING, AppointmentStatus.CONFIRMED));
        
        List<String> availableTimeSlots = new ArrayList<>();
        int treatmentDuration = treatment.getDurationMinutes();
        
        // 对每个工作时段计算可用时间
        for (DentistSchedule schedule : schedules) {
            LocalTime startTime = schedule.getStartTime();
            LocalTime endTime = schedule.getEndTime();
            
            // 添加调试日志
            System.out.println("工作时段ID: " + schedule.getId() + " 时间：" + startTime + " - " + endTime + " 于 " + dayOfWeek);
            
            // 计算可能的时间段（每30分钟一个时间段）
            LocalTime currentSlot = startTime;
            while (currentSlot.plusMinutes(treatmentDuration).isBefore(endTime) || 
                   currentSlot.plusMinutes(treatmentDuration).equals(endTime)) {
                
                // 检查此时间段是否与现有预约冲突
                boolean isAvailable = true;
                LocalTime slotEnd = currentSlot.plusMinutes(treatmentDuration);
                
                for (Appointment appointment : existingAppointments) {
                    LocalTime appointmentStart = appointment.getStartTime();
                    LocalTime appointmentEnd = appointment.getEndTime();
                    
                    // 检查时间段重叠 - 修复逻辑
                    if (!(slotEnd.compareTo(appointmentStart) <= 0 || currentSlot.compareTo(appointmentEnd) >= 0)) {
                        isAvailable = false;
                        System.out.println("时间冲突: " + currentSlot + "-" + slotEnd + " 与预约 " + appointmentStart + "-" + appointmentEnd);
                        break;
                    }
                }
                
                // 如果时间段可用，添加到结果中
                if (isAvailable) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                    availableTimeSlots.add(currentSlot.format(formatter));
                    System.out.println("添加可用时间: " + currentSlot.format(formatter));
                }
                
                // 移动到下一个30分钟时间段
                currentSlot = currentSlot.plusMinutes(30);
            }
        }
        
        // 对时间段进行排序
        Collections.sort(availableTimeSlots);
        
        return availableTimeSlots;
    }
    
    @Transactional
    public Appointment createAppointment(AppointmentDto appointmentDto, Long userId) {
        User user = userService.findById(userId);
        if (user == null) {
            throw new IllegalStateException("User not found");
        }
        
        Dentist dentist = dentistService.findById(appointmentDto.getDentistId());
        if (dentist == null) {
            throw new IllegalArgumentException("Invalid dentist selected");
        }
        
        Clinic clinic = clinicService.findById(appointmentDto.getClinicId());
        if (clinic == null) {
            throw new IllegalArgumentException("Invalid clinic selected");
        }
        
        TreatmentType treatmentType = treatmentTypeService.findById(appointmentDto.getTreatmentTypeId());
        if (treatmentType == null) {
            throw new IllegalArgumentException("Invalid treatment type selected");
        }
        
        LocalTime startTime = appointmentDto.getStartTime();
        LocalTime endTime = startTime.plusMinutes(treatmentType.getDurationMinutes());
        
        Appointment appointment = new Appointment();
        appointment.setUser(user);
        appointment.setDentist(dentist);
        appointment.setClinic(clinic);
        appointment.setTreatmentType(treatmentType);
        appointment.setAppointmentDate(appointmentDto.getAppointmentDate());
        appointment.setStartTime(startTime);
        appointment.setEndTime(endTime);
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointment.setNotes(appointmentDto.getNotes());
        
        return appointmentRepository.save(appointment);
    }
    
    @Transactional
    public Appointment updateAppointment(Long id, AppointmentDto appointmentDto, Long userId) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        
        // 验证是否为用户本人的预约
        if (!appointment.getUser().getId().equals(userId)) {
            throw new IllegalStateException("Unauthorized to update this appointment");
        }
        
        // 验证预约状态，只有待确认或已确认的预约可以修改
        if (appointment.getStatus() != AppointmentStatus.PENDING 
                && appointment.getStatus() != AppointmentStatus.CONFIRMED) {
            throw new IllegalStateException("Cannot modify appointment that is not in pending or confirmed status");
        }
        
        Dentist dentist = dentistService.findById(appointmentDto.getDentistId());
        if (dentist == null) {
            throw new IllegalArgumentException("Invalid dentist selected");
        }
        
        Clinic clinic = clinicService.findById(appointmentDto.getClinicId());
        if (clinic == null) {
            throw new IllegalArgumentException("Invalid clinic selected");
        }
        
        TreatmentType treatmentType = treatmentTypeService.findById(appointmentDto.getTreatmentTypeId());
        if (treatmentType == null) {
            throw new IllegalArgumentException("Invalid treatment type selected");
        }
        
        LocalTime startTime = appointmentDto.getStartTime();
        LocalTime endTime = startTime.plusMinutes(treatmentType.getDurationMinutes());
        
        // 更新预约信息
        appointment.setDentist(dentist);
        appointment.setClinic(clinic);
        appointment.setTreatmentType(treatmentType);
        appointment.setAppointmentDate(appointmentDto.getAppointmentDate());
        appointment.setStartTime(startTime);
        appointment.setEndTime(endTime);
        appointment.setNotes(appointmentDto.getNotes());
        
        return appointmentRepository.save(appointment);
    }
    
    @Transactional
    public void cancelAppointment(Long id, Long userId, String reason) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        
        // 验证是否为用户本人的预约
        if (!appointment.getUser().getId().equals(userId)) {
            throw new IllegalStateException("Unauthorized to cancel this appointment");
        }
        
        // 验证预约状态，只有待确认或已确认的预约可以取消
        if (appointment.getStatus() != AppointmentStatus.PENDING 
                && appointment.getStatus() != AppointmentStatus.CONFIRMED) {
            throw new IllegalStateException("Cannot cancel appointment that is not in pending or confirmed status");
        }
        
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment.setCancellationReason(reason);
        
        appointmentRepository.save(appointment);
    }
    
    public boolean canUserAccessAppointment(Long userId, Appointment appointment) {
        if (appointment == null) {
            return false;
        }
        
        return appointment.getUser().getId().equals(userId);
    }
} 