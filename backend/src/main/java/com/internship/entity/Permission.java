package com.internship.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.internship.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_permission")
public class Permission extends BaseEntity {
    private String permissionName;
    private String permissionCode;
    private String resourceType;
    private String resourcePath;
    private Long parentId;
    private Integer sort;
}
