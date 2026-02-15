package com.internship.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.internship.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("student_info")
public class StudentInfo extends BaseEntity {
    private Long userId;
    private String studentNo;
    private String className;
    private String major;
    private String college;
    private Long teacherId;
    private String teacherName;
}
