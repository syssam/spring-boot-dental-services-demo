package com.example.demo.controllers.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.models.User;
import com.example.demo.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class SigninController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/signin")
    public String index(Model model) {
        return "app/signin";
    }

    @PostMapping("/signin")
    public String submit(@RequestParam("email") String email, @RequestParam("password") String password, Model model, HttpSession session) {
        try {
            if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
                model.addAttribute("error", "电子邮箱或密码不能为空");
                return "app/signin";
            }

            // 创建认证令牌
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);
            
            // 进行认证
            Authentication authentication = authenticationManager.authenticate(authToken);
            
            // 将认证信息设置到安全上下文
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // 将SecurityContext保存到HttpSession
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, 
                                 SecurityContextHolder.getContext());
            
            // 如果选择"记住我"，可以设置更长的会话过期时间
           
            session.setMaxInactiveInterval(86400); // 设置为24小时
    
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            
            // 记录登录成功日志
            System.out.println("用户登录成功: " + email);
            
            return "redirect:/";
        } catch (AuthenticationException e) {
            // 记录详细的认证失败信息
            System.out.println("认证失败: " + e.getMessage());
            model.addAttribute("error", "电子邮箱或密码不正确");
            return "app/signin";
        } catch (Exception e) {
            // 记录详细的服务器错误
            e.printStackTrace();
            System.out.println("严重错误: " + e.getClass().getName() + ": " + e.getMessage());
            model.addAttribute("error", "服务器内部错误");
            return "app/signin";
        }
    }
}