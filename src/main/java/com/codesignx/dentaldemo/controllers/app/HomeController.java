package com.codesignx.dentaldemo.controllers.app;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.codesignx.dentaldemo.models.Meta;
import com.codesignx.dentaldemo.services.DentalServiceService;

@Controller
public class HomeController {
    private final DentalServiceService dentalServiceService;
    
    public HomeController(DentalServiceService dentalServiceService) {
        this.dentalServiceService = dentalServiceService;
    }
    
    @GetMapping("/")
    public String index(Model model) {
        List<com.codesignx.dentaldemo.models.Service> services = dentalServiceService.findAllActiveServices();
        model.addAttribute("services", services);

        Meta meta = new Meta("", "Home");
        model.addAttribute("meta", meta);

        return "app/index";
    }
}