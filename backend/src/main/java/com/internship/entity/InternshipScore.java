package com.internship.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.internship.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("internship_score")
public class InternshipScore extends BaseEntity {
    private Long studentId;
    private String studentName;
    private Long applicationId;
    private Double teacherScore;
    private Double enterpriseScore;
    private Double totalScore;
    private String teacherComment;
    private String enterpriseComment;
    private Long teacherId;
    private Long enterpriseUserId;
}
