package com.internship.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internship.dto.*;
import com.internship.entity.*;
import com.internship.mapper.*;
import com.internship.security.JwtUtils;
import com.internship.security.LoginUser;
import com.internship.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private UserRoleMapper userRoleMapper;
    
    @Autowired
    private StudentInfoMapper studentInfoMapper;
    
    @Autowired
    private TeacherInfoMapper teacherInfoMapper;
    
    @Autowired
    private EnterpriseUserMapper enterpriseUserMapper;
    
    @Autowired
    private RoleMapper roleMapper;
    
    @Override
    public LoginVO login(LoginDTO dto) {
        User user = getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, dto.getUsername()));
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        if (user.getStatus() != 1) {
            throw new RuntimeException("账号已被禁用");
        }
        
        List<String> roleNames = baseMapper.selectRoleNamesByUserId(user.getId());
        String token = jwtUtils.generateToken(user.getId(), user.getUsername(), user.getUserType());
        
        LoginVO vo = new LoginVO();
        vo.setToken(token);
        vo.setUserId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setRealName(user.getRealName());
        vo.setUserType(user.getUserType());
        vo.setRoleName(roleNames.isEmpty() ? "" : roleNames.get(0));
        return vo;
    }
    
    @Override
    @Transactional
    public void register(RegisterDTO dto) {
        Long count = count(new LambdaQueryWrapper<User>().eq(User::getUsername, dto.getUsername()));
        if (count > 0) {
            throw new RuntimeException("用户名已存在");
        }
        
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRealName(dto.getRealName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setUserType(dto.getUserType());
        user.setStatus(1);
        save(user);
        
        Role role = roleMapper.selectOne(new LambdaQueryWrapper<Role>()
            .eq(Role::getRoleCode, getRoleCodeByUserType(dto.getUserType())));
        if (role != null) {
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(role.getId());
            userRoleMapper.insert(userRole);
        }
        
        if (dto.getUserType() == 1) {
            StudentInfo studentInfo = new StudentInfo();
            studentInfo.setUserId(user.getId());
            studentInfo.setStudentNo(dto.getStudentNo());
            studentInfo.setClassName(dto.getClassName());
            studentInfo.setMajor(dto.getMajor());
            studentInfo.setCollege(dto.getCollege());
            studentInfo.setTeacherId(dto.getTeacherId());
            studentInfoMapper.insert(studentInfo);
        } else if (dto.getUserType() == 2) {
            TeacherInfo teacherInfo = new TeacherInfo();
            teacherInfo.setUserId(user.getId());
            teacherInfo.setTeacherNo(dto.getTeacherNo());
            teacherInfo.setCollege(dto.getCollege());
            teacherInfo.setDepartment(dto.getDepartment());
            teacherInfoMapper.insert(teacherInfo);
        } else if (dto.getUserType() == 3) {
            EnterpriseUser enterpriseUser = new EnterpriseUser();
            enterpriseUser.setUserId(user.getId());
            enterpriseUser.setEnterpriseId(dto.getEnterpriseId());
            enterpriseUser.setPosition(dto.getPosition());
            enterpriseUserMapper.insert(enterpriseUser);
        }
    }
    
    private String getRoleCodeByUserType(Integer userType) {
        return switch (userType) {
            case 1 -> "student";
            case 2 -> "teacher";
            case 3 -> "enterprise";
            case 4 -> "admin";
            default -> "student";
        };
    }
    
    @Override
    public UserDTO getCurrentUser() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = loginUser.getUser();
        UserDTO dto = BeanUtil.copyProperties(user, UserDTO.class);
        
        if (user.getUserType() == 1) {
            StudentInfo studentInfo = studentInfoMapper.selectOne(
                new LambdaQueryWrapper<StudentInfo>().eq(StudentInfo::getUserId, user.getId()));
            if (studentInfo != null) {
                dto.setStudentNo(studentInfo.getStudentNo());
                dto.setClassName(studentInfo.getClassName());
                dto.setMajor(studentInfo.getMajor());
                dto.setCollege(studentInfo.getCollege());
                if (studentInfo.getTeacherId() != null) {
                    TeacherInfo teacherInfo = teacherInfoMapper.selectById(studentInfo.getTeacherId());
                    if (teacherInfo != null) {
                        User teacher = getById(teacherInfo.getUserId());
                        if (teacher != null) {
                            dto.setTeacherName(teacher.getRealName());
                        }
                    }
                }
            }
        }
        return dto;
    }
    
    @Override
    public Page<UserDTO> pageUsers(PageDTO dto) {
        Page<User> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(dto.getKeyword())) {
            wrapper.like(User::getUsername, dto.getKeyword())
                   .or().like(User::getRealName, dto.getKeyword());
        }
        if (dto.getStatus() != null) {
            wrapper.eq(User::getStatus, dto.getStatus());
        }
        wrapper.orderByDesc(User::getCreateTime);
        
        Page<User> userPage = page(page, wrapper);
        Page<UserDTO> resultPage = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        List<UserDTO> records = userPage.getRecords().stream().map(user -> {
            UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
            List<String> roleNames = baseMapper.selectRoleNamesByUserId(user.getId());
            userDTO.setRoleName(roleNames.isEmpty() ? "" : roleNames.get(0));
            return userDTO;
        }).toList();
        resultPage.setRecords(records);
        return resultPage;
    }
    
    @Override
    public void updateUserStatus(Long id, Integer status) {
        User user = getById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setStatus(status);
        updateById(user);
    }
    
    @Override
    @Transactional
    public void assignRole(Long userId, Long roleId) {
        userRoleMapper.delete(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId));
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        userRoleMapper.insert(userRole);
    }
    
    @Override
    public List<String> getUserRoles(Long userId) {
        return baseMapper.selectRoleNamesByUserId(userId);
    }
    
    @Override
    @Transactional
    public void updateUserInfo(UserUpdateDTO dto) {
        User user = getById(dto.getId());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        user.setRealName(dto.getRealName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        updateById(user);
        
        if (user.getUserType() == 1) {
            StudentInfo studentInfo = studentInfoMapper.selectOne(
                new LambdaQueryWrapper<StudentInfo>().eq(StudentInfo::getUserId, user.getId()));
            if (studentInfo != null) {
                studentInfo.setStudentNo(dto.getStudentNo());
                studentInfo.setClassName(dto.getClassName());
                studentInfo.setMajor(dto.getMajor());
                studentInfo.setCollege(dto.getCollege());
                studentInfoMapper.updateById(studentInfo);
            }
        } else if (user.getUserType() == 2) {
            TeacherInfo teacherInfo = teacherInfoMapper.selectOne(
                new LambdaQueryWrapper<TeacherInfo>().eq(TeacherInfo::getUserId, user.getId()));
            if (teacherInfo != null) {
                teacherInfo.setTeacherNo(dto.getTeacherNo());
                teacherInfo.setCollege(dto.getCollege());
                teacherInfo.setDepartment(dto.getDepartment());
                teacherInfoMapper.updateById(teacherInfo);
            }
        } else if (user.getUserType() == 3) {
            EnterpriseUser enterpriseUser = enterpriseUserMapper.selectOne(
                new LambdaQueryWrapper<EnterpriseUser>().eq(EnterpriseUser::getUserId, user.getId()));
            if (enterpriseUser != null) {
                enterpriseUser.setPosition(dto.getPosition());
                enterpriseUserMapper.updateById(enterpriseUser);
            }
        }
    }
    
    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = getById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        userRoleMapper.delete(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, id));
        
        if (user.getUserType() == 1) {
            studentInfoMapper.delete(new LambdaQueryWrapper<StudentInfo>().eq(StudentInfo::getUserId, id));
        } else if (user.getUserType() == 2) {
            teacherInfoMapper.delete(new LambdaQueryWrapper<TeacherInfo>().eq(TeacherInfo::getUserId, id));
        } else if (user.getUserType() == 3) {
            enterpriseUserMapper.delete(new LambdaQueryWrapper<EnterpriseUser>().eq(EnterpriseUser::getUserId, id));
        }
        
        removeById(id);
    }
    
    @Override
    @Transactional
    public void createUser(RegisterDTO dto) {
        register(dto);
    }
    
    @Override
    public void changePassword(Long userId, PasswordDTO dto) {
        User user = getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("旧密码错误");
        }
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        updateById(user);
    }
}
