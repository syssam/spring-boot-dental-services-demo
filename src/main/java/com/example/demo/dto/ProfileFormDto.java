package com.example.demo.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProfileFormDto {
    @NotBlank
    @Size(max = 32)
    private String firstName;
    
    @NotBlank
    @Size(max = 32)
    private String lastName;
    
    @Email
    @NotBlank
    private String email;
    
    private String telephonePrefix;
    
    @NotBlank
    @Size(min = 8, max = 15)
    private String telephone;
}