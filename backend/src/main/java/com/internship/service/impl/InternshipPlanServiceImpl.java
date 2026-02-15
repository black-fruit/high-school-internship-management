package com.internship.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internship.dto.InternshipPlanDTO;
import com.internship.dto.PageDTO;
import com.internship.entity.InternshipPlan;
import com.internship.mapper.InternshipPlanMapper;
import com.internship.service.InternshipPlanService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InternshipPlanServiceImpl extends ServiceImpl<InternshipPlanMapper, InternshipPlan> implements InternshipPlanService {
    @Override
    public Page<InternshipPlanDTO> pagePlans(PageDTO dto) {
        Page<InternshipPlan> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapper<InternshipPlan> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(dto.getKeyword())) {
            wrapper.like(InternshipPlan::getPlanName, dto.getKeyword())
                   .or().like(InternshipPlan::getCollegeName, dto.getKeyword());
        }
        if (dto.getStatus() != null) {
            wrapper.eq(InternshipPlan::getStatus, dto.getStatus());
        }
        wrapper.orderByDesc(InternshipPlan::getCreateTime);
        
        Page<InternshipPlan> planPage = page(page, wrapper);
        Page<InternshipPlanDTO> resultPage = new Page<>(planPage.getCurrent(), planPage.getSize(), planPage.getTotal());
        List<InternshipPlanDTO> records = planPage.getRecords().stream()
            .map(p -> BeanUtil.copyProperties(p, InternshipPlanDTO.class))
            .toList();
        resultPage.setRecords(records);
        return resultPage;
    }
    
    @Override
    public void createPlan(InternshipPlanDTO dto, Long createBy) {
        InternshipPlan plan = BeanUtil.copyProperties(dto, InternshipPlan.class);
        plan.setStatus(0);
        plan.setCreateBy(createBy);
        save(plan);
    }
    
    @Override
    public void updatePlanStatus(Long id, Integer status) {
        InternshipPlan plan = getById(id);
        if (plan == null) {
            throw new RuntimeException("实习计划不存在");
        }
        plan.setStatus(status);
        updateById(plan);
    }
}
