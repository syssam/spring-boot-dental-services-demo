package com.example.demo.controllers.app;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.models.Meta;
import com.example.demo.services.DentalServiceService;

@Controller
public class HomeController {
    private final DentalServiceService dentalServiceService;
    
    public HomeController(DentalServiceService dentalServiceService) {
        this.dentalServiceService = dentalServiceService;
    }
    
    @GetMapping("/")
    public String index(Model model) {
        List<com.example.demo.models.Service> services = dentalServiceService.findAllActiveServices();
        model.addAttribute("services", services);

        Meta meta = new Meta("", "Home");
        model.addAttribute("meta", meta);

        return "app/index";
    }
}