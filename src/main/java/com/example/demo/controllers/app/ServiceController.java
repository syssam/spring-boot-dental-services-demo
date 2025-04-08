package com.example.demo.controllers.app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.services.DentalServiceService;

@Controller
public class ServiceController {
    
    private final DentalServiceService dentalServiceService;
    
    public ServiceController(DentalServiceService dentalServiceService) {
        this.dentalServiceService = dentalServiceService;
    }
    
    @GetMapping("/service")
    public String service(Model model) {
        model.addAttribute("services", dentalServiceService.findAllActiveServices());
        return "app/service";
    }
}
