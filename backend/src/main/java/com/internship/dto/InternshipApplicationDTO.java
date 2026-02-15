package com.internship.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class InternshipApplicationDTO implements Serializable {
    private Long id;
    private Long studentId;
    private String studentName;
    private Long positionId;
    private String positionName;
    private Long enterpriseId;
    private String enterpriseName;
    private String resume;
    private Integer status;
    private String rejectReason;
    private Long teacherId;
    private String teacherName;
    private Double teacherScore;
    private Double enterpriseScore;
}
