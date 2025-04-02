package com.example.demo.controllers.app;

import com.example.demo.services.UserService;
import com.example.demo.dto.UserRegistrationDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;

@Controller
public class SignupController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        if (!model.containsAttribute("userForm")) {
            model.addAttribute("userForm", new UserRegistrationDto());
        }
        return "app/signup";
    }
    
    @PostMapping("/signup")
    public String submit(@Valid @ModelAttribute("userForm") UserRegistrationDto userForm, 
                         BindingResult bindingResult, 
                         Model model) {
        if (bindingResult.hasErrors()) {
            return "app/signup";
        }
        
        try {
            if (userService.findByEmail(userForm.getEmail()).isPresent()) {
                model.addAttribute("error", "Email is already registered");
                return "app/signup";
            }
            
            userService.registerNewUser(userForm);
            
            return "redirect:/signin?success=registered";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "app/signup";
        }
    }
}