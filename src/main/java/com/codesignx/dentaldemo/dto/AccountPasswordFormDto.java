package com.codesignx.dentaldemo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.codesignx.dentaldemo.annotations.PasswordMatch;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = {"oldPassword", "password", "passwordConfirmation"})
@PasswordMatch(message = "New password does not match confirmation password")
public class AccountPasswordFormDto {
    
    @NotBlank(message = "Current password cannot be empty")
    private String oldPassword;
    
    @NotBlank(message = "New password cannot be empty")
    @Size(min = 8, max = 20, message = "Password length must be between 8 and 20 characters")
    private String password;
    
    @NotBlank(message = "Confirmation password cannot be empty")
    private String passwordConfirmation;
}
