package com.codesignx.dentaldemo.controllers.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.codesignx.dentaldemo.models.Meta;

import jakarta.servlet.http.HttpSession;

@Controller
public class SigninController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/signin")
    public String index(Model model) {
        Meta meta = new Meta("Signin", "Signin");
        model.addAttribute("meta", meta);  

        return "app/signin";
    }

    @PostMapping("/signin")
    public String submit(@RequestParam("email") String email, @RequestParam("password") String password, Model model, HttpSession session) {
        try {
            if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
                model.addAttribute("error", "Email or password cannot be empty");
                return "app/signin";
            }

            // Create authentication token
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);
            
            // Authenticate
            Authentication authentication = authenticationManager.authenticate(authToken);
            
            // Set authentication information to security context
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Save SecurityContext to HttpSession
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, 
                                 SecurityContextHolder.getContext());
            
            // If "Remember me" is selected, set a longer session expiration time
           
            session.setMaxInactiveInterval(86400); // Set to 24 hours
    
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            
            // Log successful login
            System.out.println("User login successful: " + userDetails.getUsername());
            
            return "redirect:/";
        } catch (AuthenticationException e) {
            // Log authentication failure details
            System.out.println("Authentication failed: " + e.getMessage());
            model.addAttribute("error", "Incorrect email or password");
            return "app/signin";
        } catch (Exception e) {
            // Log server error details
            e.printStackTrace();
            System.out.println("Critical error: " + e.getClass().getName() + ": " + e.getMessage());
            model.addAttribute("error", "Internal server error");
            return "app/signin";
        }
    }
}