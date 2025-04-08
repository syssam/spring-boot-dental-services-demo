package com.example.demo.controllers.app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.services.DentistService;

@Controller
public class TeamController {
    private final DentistService dentistService;

    public TeamController(DentistService dentistService) {
        this.dentistService = dentistService;
    }

    @GetMapping("/team")
    public String team(Model model) {
        model.addAttribute("dentists", dentistService.findAllActiveDentists());
        return "app/team";
    }
}