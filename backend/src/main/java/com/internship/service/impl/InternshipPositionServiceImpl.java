package com.internship.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internship.dto.InternshipPositionDTO;
import com.internship.dto.PageDTO;
import com.internship.entity.Enterprise;
import com.internship.entity.InternshipPosition;
import com.internship.mapper.EnterpriseMapper;
import com.internship.mapper.InternshipPositionMapper;
import com.internship.service.InternshipPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class InternshipPositionServiceImpl extends ServiceImpl<InternshipPositionMapper, InternshipPosition> implements InternshipPositionService {
    @Autowired
    private EnterpriseMapper enterpriseMapper;
    
    @Override
    public Page<InternshipPositionDTO> pagePositions(PageDTO dto) {
        Page<InternshipPosition> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapper<InternshipPosition> wrapper = new LambdaQueryWrapper<>();
        if (dto.getEnterpriseId() != null) {
            wrapper.eq(InternshipPosition::getEnterpriseId, dto.getEnterpriseId());
        }
        if (dto.getPlanId() != null) {
            wrapper.eq(InternshipPosition::getPlanId, dto.getPlanId());
        }
        if (StrUtil.isNotBlank(dto.getKeyword())) {
            wrapper.like(InternshipPosition::getPositionName, dto.getKeyword());
        }
        if (dto.getStatus() != null) {
            wrapper.eq(InternshipPosition::getStatus, dto.getStatus());
        }
        wrapper.orderByDesc(InternshipPosition::getCreateTime);
        
        Page<InternshipPosition> positionPage = page(page, wrapper);
        Page<InternshipPositionDTO> resultPage = new Page<>(positionPage.getCurrent(), positionPage.getSize(), positionPage.getTotal());
        List<InternshipPositionDTO> records = positionPage.getRecords().stream()
            .map(p -> BeanUtil.copyProperties(p, InternshipPositionDTO.class))
            .toList();
        resultPage.setRecords(records);
        return resultPage;
    }
    
    @Override
    public Page<InternshipPositionDTO> pageAvailablePositions(PageDTO dto) {
        Page<InternshipPosition> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapper<InternshipPosition> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InternshipPosition::getStatus, 1);
        wrapper.gt(InternshipPosition::getRecruitCount, 0)
               .apply("recruit_count > applied_count");
        wrapper.ge(InternshipPosition::getEndDate, LocalDate.now());
        
        if (StrUtil.isNotBlank(dto.getKeyword())) {
            wrapper.and(w -> w.like(InternshipPosition::getPositionName, dto.getKeyword())
                .or().like(InternshipPosition::getEnterpriseName, dto.getKeyword()));
        }
        wrapper.orderByDesc(InternshipPosition::getCreateTime);
        
        Page<InternshipPosition> positionPage = page(page, wrapper);
        Page<InternshipPositionDTO> resultPage = new Page<>(positionPage.getCurrent(), positionPage.getSize(), positionPage.getTotal());
        List<InternshipPositionDTO> records = positionPage.getRecords().stream()
            .map(p -> BeanUtil.copyProperties(p, InternshipPositionDTO.class))
            .toList();
        resultPage.setRecords(records);
        return resultPage;
    }
    
    @Override
    public InternshipPositionDTO getPositionDetail(Long id) {
        InternshipPosition position = getById(id);
        if (position == null) {
            throw new RuntimeException("岗位不存在");
        }
        return BeanUtil.copyProperties(position, InternshipPositionDTO.class);
    }
    
    @Override
    public void createPosition(InternshipPositionDTO dto) {
        InternshipPosition position = BeanUtil.copyProperties(dto, InternshipPosition.class);
        Enterprise enterprise = enterpriseMapper.selectById(dto.getEnterpriseId());
        if (enterprise != null) {
            position.setEnterpriseName(enterprise.getEnterpriseName());
        }
        position.setAppliedCount(0);
        position.setStatus(1);
        save(position);
    }
    
    @Override
    public void updatePositionStatus(Long id, Integer status) {
        InternshipPosition position = getById(id);
        if (position == null) {
            throw new RuntimeException("岗位不存在");
        }
        position.setStatus(status);
        updateById(position);
    }
}
