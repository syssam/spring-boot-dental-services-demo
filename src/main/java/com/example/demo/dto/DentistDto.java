package com.example.demo.dto;

import com.example.demo.models.Dentist;
import lombok.Getter;
import lombok.Setter;
import java.util.stream.Collectors;

@Getter
@Setter
public class DentistDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String specialization;
    private String email;
    private String phone;
    
    public static DentistDto fromEntity(Dentist dentist) {
        DentistDto dto = new DentistDto();
        dto.setId(dentist.getId());
        dto.setFirstName(dentist.getFirstName());
        dto.setLastName(dentist.getLastName());
        dto.setSpecialization(dentist.getSpecialization());
        dto.setEmail(dentist.getEmail());
        dto.setPhone(dentist.getPhone());
        return dto;
    }
}
