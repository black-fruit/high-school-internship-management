package com.internship.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class InternshipPlanDTO implements Serializable {
    private Long id;
    private String planName;
    private Long collegeId;
    private String collegeName;
    private String major;
    private String description;
    private String requirements;
    private String startDate;
    private String endDate;
    private Integer status;
}
