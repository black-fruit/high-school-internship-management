package com.internship.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class EnterpriseDTO implements Serializable {
    private Long id;
    private String enterpriseName;
    private String creditCode;
    private String contactPerson;
    private String contactPhone;
    private String email;
    private String address;
    private String description;
    private Integer auditStatus;
    private String auditRemark;
}
