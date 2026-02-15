package com.internship.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class InternshipScoreDTO implements Serializable {
    private Long id;
    private Long studentId;
    private String studentName;
    private Long applicationId;
    private Double teacherScore;
    private Double enterpriseScore;
    private Double totalScore;
    private String teacherComment;
    private String enterpriseComment;
}
