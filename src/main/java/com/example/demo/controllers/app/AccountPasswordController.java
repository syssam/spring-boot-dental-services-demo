package com.example.demo.controllers.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.AccountPasswordFormDto;
import com.example.demo.models.User;
import com.example.demo.services.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/account/password")
public class AccountPasswordController {

    @Autowired
    private UserService userService;
    
    @GetMapping
    public String index(Model model) {
        User authUser = userService.getAuthUser();
        if (authUser == null) {
            return "redirect:/signin";
        }
        
        if (!model.containsAttribute("passwordForm")) {
            model.addAttribute("passwordForm", new AccountPasswordFormDto());
        }
        
        return "app/account/password";
    }
    
    @PostMapping
    public String update(@Valid @ModelAttribute("passwordForm") AccountPasswordFormDto passwordForm,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        
        User authUser = userService.getAuthUser();
        if (authUser == null) {
            return "redirect:/signin";
        }
        
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.passwordForm", bindingResult);
            redirectAttributes.addFlashAttribute("passwordForm", passwordForm);
            return "redirect:/account/password";
        }
        
        try {
            boolean isPasswordChanged = userService.changePassword(
                authUser.getId(), 
                passwordForm.getOldPassword(), 
                passwordForm.getPassword()
            );
            
            if (isPasswordChanged) {
                redirectAttributes.addFlashAttribute("success", "密码已成功更新");
                return "redirect:/account";
            } else {
                redirectAttributes.addFlashAttribute("error", "当前密码不正确");
                redirectAttributes.addFlashAttribute("passwordForm", passwordForm);
                return "redirect:/account/password";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "更新密码失败: " + e.getMessage());
            redirectAttributes.addFlashAttribute("passwordForm", passwordForm);
            return "redirect:/account/password";
        }
    }
} 