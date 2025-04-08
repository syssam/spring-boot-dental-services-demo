package com.codesignx.dentaldemo.controllers.app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.codesignx.dentaldemo.models.Meta;
import com.codesignx.dentaldemo.services.DentistService;

@Controller
public class TeamController {
    private final DentistService dentistService;

    public TeamController(DentistService dentistService) {
        this.dentistService = dentistService;
    }

    @GetMapping("/team")
    public String index(Model model) {
        Meta meta = new Meta("Team", "Team");
        model.addAttribute("meta", meta);   
        model.addAttribute("dentists", dentistService.findAllActiveDentists());
        return "app/team";
    }
}