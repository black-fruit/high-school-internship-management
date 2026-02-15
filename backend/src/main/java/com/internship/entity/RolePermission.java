package com.internship.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.internship.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_role_permission")
public class RolePermission extends BaseEntity {
    private Long roleId;
    private Long permissionId;
}
