package com.codesignx.dentaldemo.controllers.app;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codesignx.dentaldemo.dto.AppointmentDto;
import com.codesignx.dentaldemo.dto.AppointmentResponseDto;
import com.codesignx.dentaldemo.dto.DentistDto;
import com.codesignx.dentaldemo.models.Appointment;
import com.codesignx.dentaldemo.models.Clinic;
import com.codesignx.dentaldemo.models.Dentist;
import com.codesignx.dentaldemo.models.TreatmentType;
import com.codesignx.dentaldemo.models.User;
import com.codesignx.dentaldemo.services.AppointmentService;
import com.codesignx.dentaldemo.services.ClinicService;
import com.codesignx.dentaldemo.services.DentistService;
import com.codesignx.dentaldemo.services.TreatmentTypeService;
import com.codesignx.dentaldemo.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class BookingController {
    
    @Autowired
    private ClinicService clinicService;
    
    @Autowired
    private DentistService dentistService;
    
    @Autowired
    private TreatmentTypeService treatmentTypeService;
    
    @Autowired
    private AppointmentService appointmentService;
    
    @Autowired
    private UserService userService;
    
    // Booking homepage - Display all user's appointment records
    @GetMapping("/booking")
    public String booking(Model model) {
        User authUser = userService.getAuthUser();
        
        if (authUser == null) {
            return "redirect:/signin";
        }
        
        // Get user's appointments
        List<Appointment> upcomingAppointments = appointmentService.findUpcomingAppointmentsByUserId(authUser.getId());
        List<Appointment> pastAppointments = appointmentService.findPastAppointmentsByUserId(authUser.getId());
        List<Appointment> cancelledAppointments = appointmentService.findCancelledAppointmentsByUserId(authUser.getId());
        
        // Add formatting tools
        model.addAttribute("dateFormatter", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        model.addAttribute("timeFormatter", DateTimeFormatter.ofPattern("HH:mm"));
        
        model.addAttribute("upcomingAppointments", upcomingAppointments);
        model.addAttribute("pastAppointments", pastAppointments);
        model.addAttribute("cancelledAppointments", cancelledAppointments);
        
        return "app/booking";
    }

    // Create booking page - Step-by-step booking process
    @GetMapping("/booking/create")
    public String createBooking(Model model) {
        User authUser = userService.getAuthUser();
        if (authUser == null) {
            return "redirect:/signin";
        }
        
        // Get all clinics
        List<Clinic> clinics = clinicService.findAllActiveClinics();
        
        // Get all service types
        List<TreatmentType> treatments = treatmentTypeService.findAllActiveTreatmentTypes();
        
        // Set bookable date range
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate threeMonthsLater = LocalDate.now().plusMonths(3);
        
        model.addAttribute("clinics", clinics);
        model.addAttribute("treatments", treatments);
        model.addAttribute("tomorrow", tomorrow.format(DateTimeFormatter.ISO_DATE));
        model.addAttribute("threeMonthsLater", threeMonthsLater.format(DateTimeFormatter.ISO_DATE));
        //model.addAttribute("user", user);
        
        return "app/booking/create";
    }

    // AJAX - Get available dentists for clinic
    @GetMapping("/api/clinics/{clinicId}/dentists")
    @ResponseBody
    public List<DentistDto> getAvailableDentists(
            @PathVariable("clinicId") Long clinicId, 
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        List<Dentist> dentists = dentistService.findAvailableDentistsByClinicAndDate(clinicId, date);
        return dentists.stream()
                .map(DentistDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    // AJAX - Get available time slots for dentist on specific date
    @GetMapping("/api/dentists/{dentistId}/timeslots")
    @ResponseBody
    public List<String> getAvailableTimeSlots(
            @PathVariable("dentistId") Long dentistId,
            @RequestParam("clinicId") Long clinicId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("treatmentId") Long treatmentId) {
        
        return appointmentService.findAvailableTimeSlots(dentistId, clinicId, date, treatmentId);
    }
    
    // AJAX - Submit appointment
    @PostMapping("/api/appointments")
    @ResponseBody
    public ResponseEntity<?> createAppointment(
            @RequestBody AppointmentDto appointmentDto,
            HttpSession session) {
        
        try {
            User authUser = userService.getAuthUser();
            Long userId = Long.valueOf(authUser.getId());
            Appointment appointment = appointmentService.createAppointment(appointmentDto, userId);
            
            // Store recently created appointment ID for redirect to confirmation page
            session.setAttribute("lastCreatedAppointmentId", appointment.getId());
            
            return ResponseEntity.ok(AppointmentResponseDto.fromEntity(appointment));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // Appointment confirmation page
    @GetMapping("/booking/confirmation")
    public String bookingConfirmation(Model model, HttpSession session) {
        Long appointmentId = (Long) session.getAttribute("lastCreatedAppointmentId");
        
        if (appointmentId == null) {
            return "redirect:/booking";
        }
        
        Appointment appointment = appointmentService.findById(appointmentId);
        model.addAttribute("appointment", appointment);
        
        // Clear appointment ID from session
        session.removeAttribute("lastCreatedAppointmentId");
        
        return "app/booking/confirmation";
    }

    // View appointment details
    @GetMapping("/booking/view/{id}")
    public String viewBooking(@PathVariable("id") Long id, Model model) {
        User authUser = userService.getAuthUser();
        if (authUser == null) {
            return "redirect:/signin";
        }
        
        Appointment appointment = appointmentService.findById(id);
        
        Long userId = Long.valueOf(authUser.getId());
        // Verify if current user has permission to view this appointment
        if (appointment == null || !appointmentService.canUserAccessAppointment(userId, appointment)) {
            return "redirect:/booking?error=unauthorized";
        }
        
        model.addAttribute("appointment", appointment);
        
        return "app/booking/view";
    }

    // Cancel appointment
    @PostMapping("/booking/cancel/{id}")
    public String cancelBooking(
            @PathVariable("id") Long id,
            @RequestParam("reason") String reason,
            @RequestParam("otherReason") String otherReason,
            RedirectAttributes redirectAttributes) {
        User authUser = userService.getAuthUser();
    if (authUser == null) {
        return "redirect:/signin";
    }
    
    try {
        Long userId = Long.valueOf(authUser.getId());
        // If "other reason" is selected, use the value from customReason field
        String finalReason = "other".equals(reason) ? otherReason : reason;
        appointmentService.cancelAppointment(id, userId, finalReason);
        redirectAttributes.addFlashAttribute("success", "Appointment has been successfully cancelled");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
    }
    
    return "redirect:/booking";
    }

    // Edit appointment page
    @GetMapping("/booking/edit/{id}")
    public String editBooking(@PathVariable("id") Long id, Model model) {
        User authUser = userService.getAuthUser();
        if (authUser == null) {
            return "redirect:/signin";
        }
        
        Appointment appointment = appointmentService.findById(id);
        
        Long userId = Long.valueOf(authUser.getId());
        // Verify if current user has permission to edit this appointment
        if (appointment == null || !appointmentService.canUserAccessAppointment(userId, appointment)) {
            return "redirect:/booking?error=unauthorized";
        }
        
        // Get all clinics
        List<Clinic> clinics = clinicService.findAllActiveClinics();
        
        // Get all service types
        List<TreatmentType> treatments = treatmentTypeService.findAllActiveTreatmentTypes();
        
        // Set bookable date range
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate threeMonthsLater = LocalDate.now().plusMonths(3);
        
        model.addAttribute("appointment", appointment);
        model.addAttribute("clinics", clinics);
        model.addAttribute("treatments", treatments);
        model.addAttribute("tomorrow", tomorrow.format(DateTimeFormatter.ISO_DATE));
        model.addAttribute("threeMonthsLater", threeMonthsLater.format(DateTimeFormatter.ISO_DATE));
        
        return "app/booking/edit";
    }

    // AJAX - Update appointment
    @PostMapping("/api/appointments/{id}")
    @ResponseBody
    public ResponseEntity<?> updateAppointment(
            @PathVariable("id") Long id,
            @RequestBody AppointmentDto appointmentDto) {
        
        try {
            User authUser = userService.getAuthUser();
            Long userId = Long.valueOf(authUser.getId());
            Appointment appointment = appointmentService.updateAppointment(id, appointmentDto, userId);
            return ResponseEntity.ok(AppointmentResponseDto.fromEntity(appointment));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}