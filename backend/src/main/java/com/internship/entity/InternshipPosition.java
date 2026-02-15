package com.internship.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.internship.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("internship_position")
public class InternshipPosition extends BaseEntity {
    private Long enterpriseId;
    private String enterpriseName;
    private Long planId;
    private String positionName;
    private String description;
    private String requirements;
    private String majorRequired;
    private Integer recruitCount;
    private Integer appliedCount;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer status;
}
