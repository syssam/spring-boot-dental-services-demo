package com.codesignx.dentaldemo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.AssertTrue;

import com.codesignx.dentaldemo.annotations.PasswordMatch;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = {"password", "passwordConfirmation"})
@PasswordMatch
public class UserRegistrationDto {
    
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;
    
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    
    @NotBlank(message = "Confirmation password cannot be empty")
    private String passwordConfirmation;
    
    @NotBlank(message = "First name cannot be empty")
    @Size(min = 2, max = 32, message = "First name must be between 2 and 32 characters")
    private String firstName;
    
    @NotBlank(message = "Last name cannot be empty")
    @Size(min = 2, max = 32, message = "Last name must be between 2 and 32 characters")
    private String lastName;
    
    @NotNull(message = "Please select a country/region code")
    @Size(max = 4, message = "Phone prefix cannot exceed 4 characters")
    private String telephonePrefix;
    
    @NotBlank(message = "Phone number cannot be empty")
    @Size(min = 8, max = 15, message = "Phone number must be between 8 and 15 digits")
    private String telephone;
    
    private boolean agree;
    
    @AssertTrue(message = "You must agree to the terms of service")
    public boolean isAgree() {
        return agree;
    }
    
    public void setAgree(boolean agree) {
        this.agree = agree;
    }
} 