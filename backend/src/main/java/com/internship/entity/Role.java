package com.internship.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.internship.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_role")
public class Role extends BaseEntity {
    private String roleName;
    private String roleCode;
    private String description;
    private Integer status;
}
