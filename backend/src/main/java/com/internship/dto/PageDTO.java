package com.internship.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageDTO implements Serializable {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String keyword;
    private Integer status;
    private Long id;
    private Long userId;
    private Long enterpriseId;
    private Long planId;
    private Long studentId;
    private Long teacherId;
    private Long positionId;
    private Long applicationId;
}
