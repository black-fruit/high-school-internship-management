package com.internship.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.internship.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("teacher_info")
public class TeacherInfo extends BaseEntity {
    private Long userId;
    private String teacherNo;
    private String college;
    private String department;
}
