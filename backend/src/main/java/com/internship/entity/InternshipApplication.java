package com.internship.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.internship.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("internship_application")
public class InternshipApplication extends BaseEntity {
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
}
