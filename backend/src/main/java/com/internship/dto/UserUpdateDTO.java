package com.internship.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserUpdateDTO implements Serializable {
    private Long id;
    private String realName;
    private String phone;
    private String email;
    private String studentNo;
    private String className;
    private String major;
    private String college;
    private String teacherNo;
    private String department;
    private String position;
}
