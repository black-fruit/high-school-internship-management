package com.internship.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.internship.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("weekly_report")
public class WeeklyReport extends BaseEntity {
    private Long studentId;
    private String studentName;
    private Long applicationId;
    private Integer weekNumber;
    private String content;
    private String teacherComment;
    private Long teacherId;
    private LocalDateTime commentTime;
}
