package com.internship.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.internship.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("internship_plan")
public class InternshipPlan extends BaseEntity {
    private String planName;
    private Long collegeId;
    private String collegeName;
    private String major;
    private String description;
    private String requirements;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer status;
    private Long createBy;
}
