package com.internship.controller;

import com.internship.common.Result;
import com.internship.dto.*;
import com.internship.security.LoginUser;
import com.internship.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO dto) {
        return Result.success(userService.login(dto));
    }
    
    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterDTO dto) {
        userService.register(dto);
        return Result.success();
    }
    
    @GetMapping("/current")
    public Result<UserDTO> getCurrentUser() {
        return Result.success(userService.getCurrentUser());
    }
    
    @PostMapping("/changePassword")
    public Result<Void> changePassword(@Valid @RequestBody PasswordDTO dto) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.changePassword(loginUser.getUser().getId(), dto);
        return Result.success();
    }
}
