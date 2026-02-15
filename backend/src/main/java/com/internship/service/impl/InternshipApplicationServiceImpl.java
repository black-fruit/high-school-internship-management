package com.internship.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internship.dto.*;
import com.internship.entity.InternshipApplication;
import com.internship.entity.InternshipPosition;
import com.internship.entity.InternshipScore;
import com.internship.entity.User;
import com.internship.mapper.InternshipApplicationMapper;
import com.internship.mapper.InternshipPositionMapper;
import com.internship.mapper.InternshipScoreMapper;
import com.internship.mapper.UserMapper;
import com.internship.service.InternshipApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InternshipApplicationServiceImpl extends ServiceImpl<InternshipApplicationMapper, InternshipApplication> implements InternshipApplicationService {
    @Autowired
    private InternshipPositionMapper positionMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private InternshipScoreMapper scoreMapper;
    
    @Override
    public Page<InternshipApplicationDTO> pageApplications(PageDTO dto) {
        Page<InternshipApplication> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapper<InternshipApplication> wrapper = new LambdaQueryWrapper<>();
        if (dto.getStatus() != null) {
            wrapper.eq(InternshipApplication::getStatus, dto.getStatus());
        }
        if (StrUtil.isNotBlank(dto.getKeyword())) {
            wrapper.like(InternshipApplication::getStudentName, dto.getKeyword())
                   .or().like(InternshipApplication::getPositionName, dto.getKeyword());
        }
        wrapper.orderByDesc(InternshipApplication::getCreateTime);
        
        Page<InternshipApplication> appPage = page(page, wrapper);
        Page<InternshipApplicationDTO> resultPage = new Page<>(appPage.getCurrent(), appPage.getSize(), appPage.getTotal());
        List<InternshipApplicationDTO> records = appPage.getRecords().stream()
            .map(a -> {
                InternshipApplicationDTO dto2 = BeanUtil.copyProperties(a, InternshipApplicationDTO.class);
                fillScoreInfo(dto2);
                return dto2;
            })
            .toList();
        resultPage.setRecords(records);
        return resultPage;
    }
    
    @Override
    public Page<InternshipApplicationDTO> pageMyApplications(Long studentId, PageDTO dto) {
        Page<InternshipApplication> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapper<InternshipApplication> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InternshipApplication::getStudentId, studentId);
        if (dto.getStatus() != null) {
            wrapper.eq(InternshipApplication::getStatus, dto.getStatus());
        }
        wrapper.orderByDesc(InternshipApplication::getCreateTime);
        
        Page<InternshipApplication> appPage = page(page, wrapper);
        Page<InternshipApplicationDTO> resultPage = new Page<>(appPage.getCurrent(), appPage.getSize(), appPage.getTotal());
        List<InternshipApplicationDTO> records = appPage.getRecords().stream()
            .map(a -> BeanUtil.copyProperties(a, InternshipApplicationDTO.class))
            .toList();
        resultPage.setRecords(records);
        return resultPage;
    }
    
    @Override
    public Page<InternshipApplicationDTO> pageEnterpriseApplications(Long enterpriseId, PageDTO dto) {
        Page<InternshipApplication> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapper<InternshipApplication> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InternshipApplication::getEnterpriseId, enterpriseId);
        if (dto.getStatus() != null) {
            wrapper.eq(InternshipApplication::getStatus, dto.getStatus());
        }
        if (StrUtil.isNotBlank(dto.getKeyword())) {
            wrapper.like(InternshipApplication::getStudentName, dto.getKeyword());
        }
        wrapper.orderByDesc(InternshipApplication::getCreateTime);
        
        Page<InternshipApplication> appPage = page(page, wrapper);
        Page<InternshipApplicationDTO> resultPage = new Page<>(appPage.getCurrent(), appPage.getSize(), appPage.getTotal());
        List<InternshipApplicationDTO> records = appPage.getRecords().stream()
            .map(a -> {
                InternshipApplicationDTO dto2 = BeanUtil.copyProperties(a, InternshipApplicationDTO.class);
                fillScoreInfo(dto2);
                return dto2;
            })
            .toList();
        resultPage.setRecords(records);
        return resultPage;
    }
    
    @Override
    public Page<InternshipApplicationDTO> pageTeacherApplications(Long teacherId, PageDTO dto) {
        Page<InternshipApplication> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapper<InternshipApplication> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InternshipApplication::getTeacherId, teacherId);
        if (dto.getStatus() != null) {
            wrapper.eq(InternshipApplication::getStatus, dto.getStatus());
        }
        if (StrUtil.isNotBlank(dto.getKeyword())) {
            wrapper.like(InternshipApplication::getStudentName, dto.getKeyword());
        }
        wrapper.orderByDesc(InternshipApplication::getCreateTime);
        
        Page<InternshipApplication> appPage = page(page, wrapper);
        Page<InternshipApplicationDTO> resultPage = new Page<>(appPage.getCurrent(), appPage.getSize(), appPage.getTotal());
        List<InternshipApplicationDTO> records = appPage.getRecords().stream()
            .map(a -> {
                InternshipApplicationDTO dto2 = BeanUtil.copyProperties(a, InternshipApplicationDTO.class);
                fillScoreInfo(dto2);
                return dto2;
            })
            .toList();
        resultPage.setRecords(records);
        return resultPage;
    }
    
    @Override
    @Transactional
    public void apply(Long studentId, Long positionId, String resume) {
        InternshipApplication existing = getByStudentAndPosition(studentId, positionId);
        if (existing != null) {
            throw new RuntimeException("您已申请过该岗位");
        }
        
        InternshipPosition position = positionMapper.selectById(positionId);
        if (position == null) {
            throw new RuntimeException("岗位不存在");
        }
        if (position.getAppliedCount() >= position.getRecruitCount()) {
            throw new RuntimeException("该岗位已招满");
        }
        
        User student = userMapper.selectById(studentId);
        
        InternshipApplication application = new InternshipApplication();
        application.setStudentId(studentId);
        application.setStudentName(student.getRealName());
        application.setPositionId(positionId);
        application.setPositionName(position.getPositionName());
        application.setEnterpriseId(position.getEnterpriseId());
        application.setEnterpriseName(position.getEnterpriseName());
        application.setResume(resume);
        application.setStatus(0);
        save(application);
        
        position.setAppliedCount(position.getAppliedCount() + 1);
        positionMapper.updateById(position);
    }
    
    @Override
    public void approve(Long id, Long teacherId, String teacherName) {
        InternshipApplication application = getById(id);
        if (application == null) {
            throw new RuntimeException("申请不存在");
        }
        application.setStatus(1);
        application.setTeacherId(teacherId);
        application.setTeacherName(teacherName);
        updateById(application);
    }
    
    @Override
    public void reject(Long id, String rejectReason) {
        InternshipApplication application = getById(id);
        if (application == null) {
            throw new RuntimeException("申请不存在");
        }
        application.setStatus(2);
        application.setRejectReason(rejectReason);
        updateById(application);
    }
    
    @Override
    public InternshipApplication getByStudentAndPosition(Long studentId, Long positionId) {
        return getOne(new LambdaQueryWrapper<InternshipApplication>()
            .eq(InternshipApplication::getStudentId, studentId)
            .eq(InternshipApplication::getPositionId, positionId));
    }
    
    private void fillScoreInfo(InternshipApplicationDTO dto) {
        InternshipScore score = scoreMapper.selectOne(
            new LambdaQueryWrapper<InternshipScore>().eq(InternshipScore::getApplicationId, dto.getId()));
        if (score != null) {
            dto.setTeacherScore(score.getTeacherScore());
            dto.setEnterpriseScore(score.getEnterpriseScore());
        }
    }
}
