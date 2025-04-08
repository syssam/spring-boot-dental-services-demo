package com.example.demo.controllers.app;

import com.example.demo.models.Contact;
import com.example.demo.repositories.ContactRepository;
import com.example.demo.services.ClinicService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
public class ContactController {
    
    @Autowired
    private ContactRepository contactRepository;
    
    @Autowired
    private ClinicService clinicService;
    
    @GetMapping("/contact")
    public String index(Model model) {
        if (!model.containsAttribute("contact")) {
            model.addAttribute("contact", new Contact());
        }
        model.addAttribute("clinics", clinicService.findAllActiveClinics());
        return "app/contact";
    }
    
    @PostMapping("/contact")
    public String submit(@Valid @ModelAttribute Contact contact, 
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("contact", contact);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.contact", bindingResult);
            System.out.println("fieldErrors: " + bindingResult.getFieldErrors());
            return "redirect:/contact";
        }
        
        try {
            contactRepository.save(contact);
            redirectAttributes.addFlashAttribute("success", "您的消息已成功提交，我们会尽快回复您！");
            System.out.println("success");
            return "redirect:/contact";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "提交失败：" + e.getMessage());
            redirectAttributes.addFlashAttribute("contact", contact);
            System.out.println("提交失败：" + e.getMessage());
            return "redirect:/contact";
        }
    }
}