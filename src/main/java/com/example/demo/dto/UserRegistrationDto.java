package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.AssertTrue;

import com.example.demo.annotations.PasswordMatch;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = {"password", "passwordConfirmation"})
@PasswordMatch
public class UserRegistrationDto {
    
    @NotBlank(message = "电子邮箱不能为空")
    @Email(message = "电子邮箱格式不正确")
    private String email;
    
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, message = "密码长度至少为6位")
    private String password;
    
    @NotBlank(message = "确认密码不能为空")
    private String passwordConfirmation;
    
    @NotBlank(message = "名字不能为空")
    @Size(min = 2, max = 50, message = "名字长度必须在2到50个字符之间")
    private String firstName;
    
    @NotBlank(message = "姓氏不能为空")
    @Size(min = 2, max = 50, message = "姓氏长度必须在2到50个字符之间")
    private String lastName;
    
    @NotNull(message = "请选择电话国家/地区代码")
    @Size(max = 4, message = "电话前缀不能超过4个字符")
    private String telephonePrefix;
    
    @NotBlank(message = "电话号码不能为空")
    @Pattern(regexp = "^\\d{6,11}$", message = "手机号格式不正确")
    private String telephone;
    
    private boolean agree;
    
    @AssertTrue(message = "必须同意服务条款")
    public boolean isAgree() {
        return agree;
    }
    
    public void setAgree(boolean agree) {
        this.agree = agree;
    }
} 