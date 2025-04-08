package com.codesignx.dentaldemo.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, Object> {
    
    private String passwordField;
    private String passwordConfirmationField;
    private String message;

    @Override
    public void initialize(PasswordMatch constraintAnnotation) {
        this.passwordField = constraintAnnotation.password();
        this.passwordConfirmationField = constraintAnnotation.passwordConfirmation();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object passwordValue = new BeanWrapperImpl(value).getPropertyValue(passwordField);
        Object confirmationValue = new BeanWrapperImpl(value).getPropertyValue(passwordConfirmationField);
        
        boolean isValid = false;
        
        if (passwordValue != null) {
            isValid = passwordValue.equals(confirmationValue);
        }
        
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                   .addConstraintViolation();
                   
            context.buildConstraintViolationWithTemplate(message)
                   .addPropertyNode(passwordConfirmationField)
                   .addConstraintViolation();
        }
        
        return isValid;
    }
} 