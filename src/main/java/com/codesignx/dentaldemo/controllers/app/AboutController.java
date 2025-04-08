package com.codesignx.dentaldemo.controllers.app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.codesignx.dentaldemo.models.Meta;

@Controller
public class AboutController {
    @GetMapping("/about")
    public String index(Model model) {
        Meta meta = new Meta("About", "About");
        model.addAttribute("meta", meta);

        return "app/about";
    }
}