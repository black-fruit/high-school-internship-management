package com.internship.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.internship.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("enterprise_user")
public class EnterpriseUser extends BaseEntity {
    private Long userId;
    private Long enterpriseId;
    private String position;
}
