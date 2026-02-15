package com.internship.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDTO implements Serializable {
    private Long id;
    private String username;
    private String realName;
    private String phone;
    private String email;
    private Integer userType;
    private Integer status;
    private String studentNo;
    private String className;
    private String major;
    private String college;
    private String teacherName;
    private String roleName;
}
