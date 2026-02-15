package com.internship.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginVO implements Serializable {
    private String token;
    private Long userId;
    private String username;
    private String realName;
    private Integer userType;
    private String roleName;
}
