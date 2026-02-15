package com.internship.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.internship.common.Result;
import com.internship.dto.*;
import com.internship.entity.Role;
import com.internship.mapper.RoleMapper;
import com.internship.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private RoleMapper roleMapper;
    
    @GetMapping("/page")
    public Result<Page<UserDTO>> pageUsers(PageDTO dto) {
        return Result.success(userService.pageUsers(dto));
    }
    
    @GetMapping("/{id}")
    public Result<UserDTO> getUser(@PathVariable Long id) {
        return Result.success(userService.getCurrentUser());
    }
    
    @PostMapping
    public Result<Void> createUser(@Valid @RequestBody RegisterDTO dto) {
        userService.createUser(dto);
        return Result.success();
    }
    
    @PutMapping
    public Result<Void> updateUser(@RequestBody UserUpdateDTO dto) {
        userService.updateUserInfo(dto);
        return Result.success();
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success();
    }
    
    @PutMapping("/status/{id}")
    public Result<Void> updateUserStatus(@PathVariable Long id, @RequestParam Integer status) {
        userService.updateUserStatus(id, status);
        return Result.success();
    }
    
    @PostMapping("/assignRole")
    public Result<Void> assignRole(@RequestParam Long userId, @RequestParam Long roleId) {
        userService.assignRole(userId, roleId);
        return Result.success();
    }
    
    @GetMapping("/roles")
    public Result<List<Role>> getRoles() {
        return Result.success(roleMapper.selectList(null));
    }
    
    @GetMapping("/roles/{userId}")
    public Result<List<String>> getUserRoles(@PathVariable Long userId) {
        return Result.success(userService.getUserRoles(userId));
    }
}
