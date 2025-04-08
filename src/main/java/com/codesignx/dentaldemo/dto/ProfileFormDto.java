package com.codesignx.dentaldemo.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProfileFormDto {
    @NotBlank(message = "First name cannot be empty")
    @Size(min = 2, max = 32, message = "First name must be between 2 and 32 characters")
    private String firstName;
    
    @NotBlank(message = "Last name cannot be empty")
    @Size(min = 2, max = 32, message = "Last name must be between 2 and 32 characters")
    private String lastName;
    
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be empty")
    private String email;
    
    @NotBlank(message = "Phone prefix cannot be empty")
    @Size(max = 4, message = "Phone prefix cannot exceed 4 characters")
    private String telephonePrefix;
    
    @NotBlank(message = "Phone number cannot be empty")
    @Size(min = 8, max = 15, message = "Phone number must be between 8 and 15 digits")
    private String telephone;
}