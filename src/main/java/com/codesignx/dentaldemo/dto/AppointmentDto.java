package com.codesignx.dentaldemo.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AppointmentDto {
    private Long userId;
    private Long dentistId;
    private Long clinicId;
    private Long treatmentTypeId;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate appointmentDate;
    
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    
    private String notes;
    
    // Getters and Setters are already provided by Lombok @Getter @Setter
} 