package com.internship.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.internship.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("internship_report")
public class InternshipReport extends BaseEntity {
    private Long studentId;
    private String studentName;
    private Long applicationId;
    private String title;
    private String content;
    private String attachment;
    private LocalDateTime submitTime;
}
