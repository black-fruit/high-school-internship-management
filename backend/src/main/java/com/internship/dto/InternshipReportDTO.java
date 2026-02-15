package com.internship.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class InternshipReportDTO implements Serializable {
    private Long id;
    private Long studentId;
    private String studentName;
    private Long applicationId;
    private String title;
    private String content;
    private String attachment;
    private String submitTime;
}
