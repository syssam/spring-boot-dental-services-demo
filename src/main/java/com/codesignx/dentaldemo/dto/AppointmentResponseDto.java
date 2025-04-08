package com.codesignx.dentaldemo.dto;

import com.codesignx.dentaldemo.models.Appointment;
import com.codesignx.dentaldemo.models.AppointmentStatus;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentResponseDto {
    private Long id;
    private Long userId;
    private String userName;
    private Long dentistId;
    private String dentistName;
    private Long clinicId;
    private String clinicName;
    private Long treatmentTypeId;
    private String treatmentName;
    private LocalDate appointmentDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private AppointmentStatus status;
    private String notes;
    
    public static AppointmentResponseDto fromEntity(Appointment appointment) {
        AppointmentResponseDto dto = new AppointmentResponseDto();
        dto.setId(appointment.getId());
        dto.setUserId(appointment.getUser().getId());
        dto.setUserName(appointment.getUser().getFirstName() + " " + appointment.getUser().getLastName());
        dto.setDentistId(appointment.getDentist().getId());
        dto.setDentistName("Dr. " + appointment.getDentist().getName());
        dto.setClinicId(appointment.getClinic().getId());
        dto.setClinicName(appointment.getClinic().getName());
        dto.setTreatmentTypeId(appointment.getTreatmentType().getId());
        dto.setTreatmentName(appointment.getTreatmentType().getName());
        dto.setAppointmentDate(appointment.getAppointmentDate());
        dto.setStartTime(appointment.getStartTime());
        dto.setEndTime(appointment.getEndTime());
        dto.setStatus(appointment.getStatus());
        dto.setNotes(appointment.getNotes());
        return dto;
    }
}
