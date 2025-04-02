package com.example.demo.controllers.app;

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

import com.example.demo.dto.AppointmentDto;
import com.example.demo.dto.AppointmentResponseDto;
import com.example.demo.dto.DentistDto;
import com.example.demo.models.Appointment;
import com.example.demo.models.Clinic;
import com.example.demo.models.Dentist;
import com.example.demo.models.TreatmentType;
import com.example.demo.models.User;
import com.example.demo.services.AppointmentService;
import com.example.demo.services.ClinicService;
import com.example.demo.services.DentistService;
import com.example.demo.services.TreatmentTypeService;
import com.example.demo.services.UserService;

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
    
    // 预约首页 - 显示用户的所有预约记录
    @GetMapping("/booking")
    public String booking(Model model) {
        User authUser = userService.getAuthUser();
        
        if (authUser == null) {
            return "redirect:/signin";
        }
        
        // 获取用户的预约
        List<Appointment> upcomingAppointments = appointmentService.findUpcomingAppointmentsByUserId(authUser.getId());
        List<Appointment> pastAppointments = appointmentService.findPastAppointmentsByUserId(authUser.getId());
        List<Appointment> cancelledAppointments = appointmentService.findCancelledAppointmentsByUserId(authUser.getId());
        
        // 添加格式化工具
        model.addAttribute("dateFormatter", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        model.addAttribute("timeFormatter", DateTimeFormatter.ofPattern("HH:mm"));
        
        model.addAttribute("upcomingAppointments", upcomingAppointments);
        model.addAttribute("pastAppointments", pastAppointments);
        model.addAttribute("cancelledAppointments", cancelledAppointments);
        
        return "app/booking";
    }

    // 创建预约页面 - 步骤式预约流程
    @GetMapping("/booking/create")
    public String createBooking(Model model) {
        User authUser = userService.getAuthUser();
        if (authUser == null) {
            return "redirect:/signin";
        }
        
        // 获取所有诊所
        List<Clinic> clinics = clinicService.findAllActiveClinics();
        
        // 获取所有服务类型
        List<TreatmentType> treatments = treatmentTypeService.findAllActiveTreatmentTypes();
        
        // 设置可预约日期范围
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate threeMonthsLater = LocalDate.now().plusMonths(3);
        
        model.addAttribute("clinics", clinics);
        model.addAttribute("treatments", treatments);
        model.addAttribute("tomorrow", tomorrow.format(DateTimeFormatter.ISO_DATE));
        model.addAttribute("threeMonthsLater", threeMonthsLater.format(DateTimeFormatter.ISO_DATE));
        //model.addAttribute("user", user);
        
        return "app/booking/create";
    }

    // AJAX - 获取诊所的可用牙医
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
    
    // AJAX - 获取牙医在特定日期的可用时间段
    @GetMapping("/api/dentists/{dentistId}/timeslots")
    @ResponseBody
    public List<String> getAvailableTimeSlots(
            @PathVariable("dentistId") Long dentistId,
            @RequestParam("clinicId") Long clinicId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("treatmentId") Long treatmentId) {
        
        return appointmentService.findAvailableTimeSlots(dentistId, clinicId, date, treatmentId);
    }
    
    // AJAX - 提交预约
    @PostMapping("/api/appointments")
    @ResponseBody
    public ResponseEntity<?> createAppointment(
            @RequestBody AppointmentDto appointmentDto,
            HttpSession session) {
        
        try {
            User authUser = userService.getAuthUser();
            Long userId = Long.valueOf(authUser.getId());
            Appointment appointment = appointmentService.createAppointment(appointmentDto, userId);
            
            // 存储最近创建的预约ID，用于重定向到预约确认页面
            session.setAttribute("lastCreatedAppointmentId", appointment.getId());
            
            return ResponseEntity.ok(AppointmentResponseDto.fromEntity(appointment));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // 预约确认页面
    @GetMapping("/booking/confirmation")
    public String bookingConfirmation(Model model, HttpSession session) {
        Long appointmentId = (Long) session.getAttribute("lastCreatedAppointmentId");
        
        if (appointmentId == null) {
            return "redirect:/booking";
        }
        
        Appointment appointment = appointmentService.findById(appointmentId);
        model.addAttribute("appointment", appointment);
        
        // 清除会话中的预约ID
        session.removeAttribute("lastCreatedAppointmentId");
        
        return "app/booking/confirmation";
    }

    // 查看预约详情
    @GetMapping("/booking/view/{id}")
    public String viewBooking(@PathVariable("id") Long id, Model model) {
        User authUser = userService.getAuthUser();
        if (authUser == null) {
            return "redirect:/signin";
        }
        
        Appointment appointment = appointmentService.findById(id);
        
        Long userId = Long.valueOf(authUser.getId());
        // 验证当前用户是否有权限查看此预约
        if (appointment == null || !appointmentService.canUserAccessAppointment(userId, appointment)) {
            return "redirect:/booking?error=unauthorized";
        }
        
        model.addAttribute("appointment", appointment);
        
        return "app/booking/view";
    }

    // 取消预约
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
        // 如果是"其他原因"，则使用customReason字段的值
        String finalReason = "other".equals(reason) ? otherReason : reason;
        appointmentService.cancelAppointment(id, userId, finalReason);
        redirectAttributes.addFlashAttribute("success", "预约已成功取消");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
    }
    
    return "redirect:/booking";
    }

    // 预约修改页面
    @GetMapping("/booking/edit/{id}")
    public String editBooking(@PathVariable("id") Long id, Model model) {
        User authUser = userService.getAuthUser();
        if (authUser == null) {
            return "redirect:/signin";
        }
        
        Appointment appointment = appointmentService.findById(id);
        
        Long userId = Long.valueOf(authUser.getId());
        // 验证当前用户是否有权限编辑此预约
        if (appointment == null || !appointmentService.canUserAccessAppointment(userId, appointment)) {
            return "redirect:/booking?error=unauthorized";
        }
        
        // 获取所有诊所
        List<Clinic> clinics = clinicService.findAllActiveClinics();
        
        // 获取所有服务类型
        List<TreatmentType> treatments = treatmentTypeService.findAllActiveTreatmentTypes();
        
        // 设置可预约日期范围
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate threeMonthsLater = LocalDate.now().plusMonths(3);
        
        model.addAttribute("appointment", appointment);
        model.addAttribute("clinics", clinics);
        model.addAttribute("treatments", treatments);
        model.addAttribute("tomorrow", tomorrow.format(DateTimeFormatter.ISO_DATE));
        model.addAttribute("threeMonthsLater", threeMonthsLater.format(DateTimeFormatter.ISO_DATE));
        
        return "app/booking/edit";
    }

    // AJAX - 更新预约
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