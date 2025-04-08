package com.codesignx.dentaldemo.controllers.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codesignx.dentaldemo.dto.ProfileFormDto;
import com.codesignx.dentaldemo.models.User;
import com.codesignx.dentaldemo.models.Meta;
import com.codesignx.dentaldemo.services.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private UserService userService;
    
    
    @GetMapping
    public String index(Model model) {
        User authUser = userService.getAuthUser();
        if (authUser == null) {
            return "redirect:/signin";
        }
        
        Meta meta = new Meta("Account", "Account");
        model.addAttribute("meta", meta);

        if (!model.containsAttribute("profileForm")) {
            ProfileFormDto profileForm = new ProfileFormDto();
            profileForm.setFirstName(authUser.getFirstName());
            profileForm.setLastName(authUser.getLastName());
            profileForm.setEmail(authUser.getEmail());
            profileForm.setTelephone(authUser.getTelephone());
            profileForm.setTelephonePrefix(authUser.getTelephonePrefix());
            
            model.addAttribute("profileForm", profileForm);
        }
        
        return "app/account/profile";
    }
    
    @PostMapping
    public String submit(@Valid @ModelAttribute("profileForm") ProfileFormDto profileForm,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        
        User authUser = userService.getAuthUser();
        if (authUser == null) {
            return "redirect:/signin";
        }
        
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.profileForm", bindingResult);
            redirectAttributes.addFlashAttribute("profileForm", profileForm);
            return "redirect:/account";
        }
        
        try {
            userService.updateProfile(authUser.getId(), profileForm);
            redirectAttributes.addFlashAttribute("success", "个人资料更新成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "更新失败: " + e.getMessage());
        }
        
        return "redirect:/account";
    }
} 