package com.example.demo.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.Appointment;

@Service
public class BookingService {

    private final AppointmentService appointmentService;
    
    public BookingService(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }
    
    public List<Appointment> getUpcomingAppointments(Long userId) {
        return appointmentService.findUpcomingAppointmentsByUserId(userId);
    }
    
    public List<Appointment> getPastAppointments(Long userId) {
        return appointmentService.findPastAppointmentsByUserId(userId);
    }
    
    public List<Appointment> getCancelledAppointments(Long userId) {
        return appointmentService.findCancelledAppointmentsByUserId(userId);
    }
    
    public Appointment getAppointmentDetails(Long appointmentId, Long userId) {
        Appointment appointment = appointmentService.findById(appointmentId);
        if (appointment == null || !appointmentService.canUserAccessAppointment(userId, appointment)) {
            return null;
        }
        return appointment;
    }
    
    @Transactional
    public void cancelAppointment(Long appointmentId, Long userId, String reason) {
        appointmentService.cancelAppointment(appointmentId, userId, reason);
    }
} 