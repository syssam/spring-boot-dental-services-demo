package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.UserRegistrationDto;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UserService(
            UserRepository userRepository, 
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    
    @Transactional
    public User registerNewUser(UserRegistrationDto registrationDto) {
        // 验证邮箱未被使用
        if (userRepository.findByEmail(registrationDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("邮箱已被注册");
        }
        
        // 创建并保存用户
        User user = new User();
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        user.setTelephone(registrationDto.getTelephone());
        user.setTelephonePrefix(registrationDto.getTelephonePrefix());
        user.setStatus(true);
        user = userRepository.save(user);

        return user;
    }
    
    @Transactional
    public User updateProfile(Long userId, UserRegistrationDto profileDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        
        // 更新用户基本信息
        user.setFirstName(profileDto.getFirstName());
        user.setLastName(profileDto.getLastName());
        user.setTelephone(profileDto.getTelephone());
        user.setTelephonePrefix(profileDto.getTelephonePrefix());
        // 邮箱不允许更改
        
        // 如果提供了新密码则更新密码
        if (profileDto.getPassword() != null && !profileDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(profileDto.getPassword()));
        }
        
        return userRepository.save(user);
    }
    
    public boolean checkPassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));
            
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        
        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            user.isActive(),
            true, // account non-expired
            true, // credentials non-expired
            true, // account non-locked
            authorities
        );
    }

    public User getAuthUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            System.out.println("DEBUG: 认证对象为空");
            return null;
        }
        
        if (!authentication.isAuthenticated()) {
            System.out.println("DEBUG: 用户未认证");
            return null;
        }
    
        if ("anonymousUser".equals(authentication.getPrincipal())) {
            System.out.println("DEBUG: 匿名用户");
            return null;
        }

        String email = authentication.getName();
        System.out.println("DEBUG: 认证用户邮箱: " + email);
    
        // 通过邮箱查找用户
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            System.out.println("DEBUG: 未找到邮箱对应的用户: " + email);
        } else {
            System.out.println("DEBUG: 找到用户: ID=" + user.getId() + ", Email=" + user.getEmail());
        }
        
        // 使用邮箱查找用户实体
        return user;
    
    }
} 