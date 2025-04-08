package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.example.demo.annotations.PasswordMatch;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = {"oldPassword", "password", "passwordConfirmation"})
@PasswordMatch(message = "新密码与确认密码不匹配")
public class AccountPasswordFormDto {
    
    @NotBlank(message = "当前密码不能为空")
    private String oldPassword;
    
    @NotBlank(message = "新密码不能为空")
    @Size(min = 8, max = 20, message = "密码长度必须在8到20个字符之间")
    private String password;
    
    @NotBlank(message = "确认密码不能为空")
    private String passwordConfirmation;
}
