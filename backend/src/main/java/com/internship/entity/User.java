package com.internship.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.internship.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class User extends BaseEntity {
    private String username;
    private String password;
    private String realName;
    private String phone;
    private String email;
    private Integer userType;
    private Integer status;
    
    @TableField(exist = false)
    private String roleName;
}
