package com.codesignx.dentaldemo.dto;

import com.codesignx.dentaldemo.models.Dentist;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DentistDto {
    private Long id;
    private String name;
    private String specialization;
    
    public static DentistDto fromEntity(Dentist dentist) {
        DentistDto dto = new DentistDto();
        dto.setId(dentist.getId());
        dto.setName(dentist.getName());
        dto.setSpecialization(dentist.getSpecialization());
        return dto;
    }
}
