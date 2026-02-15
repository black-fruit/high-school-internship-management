package com.internship.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.internship.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("enterprise")
public class Enterprise extends BaseEntity {
    private String enterpriseName;
    private String creditCode;
    private String contactPerson;
    private String contactPhone;
    private String email;
    private String address;
    private String description;
    private Integer auditStatus;
    private Long auditBy;
    private String auditRemark;
}
