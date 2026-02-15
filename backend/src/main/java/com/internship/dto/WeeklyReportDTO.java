package com.internship.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class WeeklyReportDTO implements Serializable {
    private Long id;
    private Long studentId;
    private String studentName;
    private Long applicationId;
    private Integer weekNumber;
    private String content;
    private String teacherComment;
    private String commentTime;
}
