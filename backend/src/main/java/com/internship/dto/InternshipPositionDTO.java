package com.internship.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class InternshipPositionDTO implements Serializable {
    private Long id;
    private Long enterpriseId;
    private String enterpriseName;
    private Long planId;
    private String positionName;
    private String description;
    private String requirements;
    private String majorRequired;
    private Integer recruitCount;
    private Integer appliedCount;
    private String startDate;
    private String endDate;
    private Integer status;
}
