package com.internship.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.internship.dto.*;
import com.internship.entity.User;

import java.util.List;

public interface UserService extends IService<User> {
    LoginVO login(LoginDTO dto);
    void register(RegisterDTO dto);
    UserDTO getCurrentUser();
    Page<UserDTO> pageUsers(PageDTO dto);
    void updateUserStatus(Long id, Integer status);
    void assignRole(Long userId, Long roleId);
    List<String> getUserRoles(Long userId);
    void updateUserInfo(UserUpdateDTO dto);
    void deleteUser(Long id);
    void createUser(RegisterDTO dto);
    void changePassword(Long userId, PasswordDTO dto);
}
