package com.codesignx.dentaldemo.controllers.app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.codesignx.dentaldemo.models.Meta;
import com.codesignx.dentaldemo.services.DentalServiceService;

@Controller
public class ServiceController {
    
    private final DentalServiceService dentalServiceService;
    
    public ServiceController(DentalServiceService dentalServiceService) {
        this.dentalServiceService = dentalServiceService;
    }
    
    @GetMapping("/service")
    public String index(Model model) {
        Meta meta = new Meta("Service", "Service");
        model.addAttribute("meta", meta);   

        model.addAttribute("services", dentalServiceService.findAllActiveServices());
        return "app/service";
    }
}
