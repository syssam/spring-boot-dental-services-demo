package com.codesignx.dentaldemo.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesignx.dentaldemo.dto.AppointmentDto;
import com.codesignx.dentaldemo.models.Appointment;
import com.codesignx.dentaldemo.models.AppointmentStatus;
import com.codesignx.dentaldemo.models.Clinic;
import com.codesignx.dentaldemo.models.Dentist;
import com.codesignx.dentaldemo.models.DentistSchedule;
import com.codesignx.dentaldemo.models.TreatmentType;
import com.codesignx.dentaldemo.models.User;
import com.codesignx.dentaldemo.repositories.AppointmentRepository;
import com.codesignx.dentaldemo.repositories.DentistScheduleRepository;
import com.codesignx.dentaldemo.models.DentistSchedule.DayOfWeek;

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
        // Only get completed and no-show appointments with dates in the past
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
        // Get dentist, clinic and treatment information
        Dentist dentist = dentistService.findById(dentistId);
        Clinic clinic = clinicService.findById(clinicId);
        TreatmentType treatment = treatmentTypeService.findById(treatmentId);
        
        if (dentist == null || clinic == null || treatment == null) {
            return new ArrayList<>();
        }
        
        // Get dentist's work schedule for the clinic and date
        java.time.DayOfWeek javaDayOfWeek = date.getDayOfWeek();
        DayOfWeek dayOfWeek = DayOfWeek.valueOf(javaDayOfWeek.name());
        
        System.out.println("Querying dentist ID: " + dentistId + " clinic ID: " + clinicId + " date: " + date + " day: " + dayOfWeek);
        List<DentistSchedule> schedules = dentistScheduleRepository.findByDentistIdAndClinicIdAndDayOfWeekAndActiveTrue(
                dentistId, clinicId, dayOfWeek);
        
        System.out.println("Found work slots count: " + schedules.size());
        if (schedules.isEmpty()) {
            System.out.println("Warning: No work slots found for this dentist at this clinic!");
            return new ArrayList<>();
        }
        
        // Get existing appointments for the dentist on this date
        List<Appointment> existingAppointments = appointmentRepository.findByDentistIdAndClinicIdAndAppointmentDateAndStatusIn(
                dentistId, clinicId, date, List.of(AppointmentStatus.PENDING, AppointmentStatus.CONFIRMED));
        
        List<String> availableTimeSlots = new ArrayList<>();
        int treatmentDuration = treatment.getDurationMinutes();
        
        // Calculate available times for each work slot
        for (DentistSchedule schedule : schedules) {
            LocalTime startTime = schedule.getStartTime();
            LocalTime endTime = schedule.getEndTime();
            
            // Add debug log
            System.out.println("Work slot ID: " + schedule.getId() + " time: " + startTime + " - " + endTime + " on " + dayOfWeek);
            
            // Calculate possible time slots (one slot every 30 minutes)
            LocalTime currentSlot = startTime;
            while (currentSlot.plusMinutes(treatmentDuration).isBefore(endTime) || 
                   currentSlot.plusMinutes(treatmentDuration).equals(endTime)) {
                
                // Check if this time slot conflicts with existing appointments
                boolean isAvailable = true;
                LocalTime slotEnd = currentSlot.plusMinutes(treatmentDuration);
                
                for (Appointment appointment : existingAppointments) {
                    LocalTime appointmentStart = appointment.getStartTime();
                    LocalTime appointmentEnd = appointment.getEndTime();
                    
                    // Check for time slot overlap - fixed logic
                    if (!(slotEnd.compareTo(appointmentStart) <= 0 || currentSlot.compareTo(appointmentEnd) >= 0)) {
                        isAvailable = false;
                        System.out.println("Time conflict: " + currentSlot + "-" + slotEnd + " with appointment " + appointmentStart + "-" + appointmentEnd);
                        break;
                    }
                }
                
                // If time slot is available, add to results
                if (isAvailable) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                    availableTimeSlots.add(currentSlot.format(formatter));
                    System.out.println("Added available time: " + currentSlot.format(formatter));
                }
                
                // Move to next 30-minute time slot
                currentSlot = currentSlot.plusMinutes(30);
            }
        }
        
        // Sort the time slots
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
        
        // Verify if it's the user's own appointment
        if (!appointment.getUser().getId().equals(userId)) {
            throw new IllegalStateException("Unauthorized to update this appointment");
        }
        
        // Verify appointment status, only pending or confirmed appointments can be modified
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
        
        // Update appointment information
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
        
        // Verify if it's the user's own appointment
        if (!appointment.getUser().getId().equals(userId)) {
            throw new IllegalStateException("Unauthorized to cancel this appointment");
        }
        
        // Verify appointment status, only pending or confirmed appointments can be cancelled
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