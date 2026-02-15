package com.internship.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
public class RegisterDTO implements Serializable {
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    @NotBlank(message = "密码不能为空")
    private String password;
    
    @NotBlank(message = "真实姓名不能为空")
    private String realName;
    
    private String phone;
    private String email;
    
    private Integer userType;
    
    private String studentNo;
    private String className;
    private String major;
    private String college;
    private Long teacherId;
    
    private String teacherNo;
    private String department;
    
    private Long enterpriseId;
    private String position;
}
