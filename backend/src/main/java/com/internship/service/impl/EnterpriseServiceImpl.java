package com.internship.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internship.dto.EnterpriseDTO;
import com.internship.dto.PageDTO;
import com.internship.entity.Enterprise;
import com.internship.entity.EnterpriseUser;
import com.internship.mapper.EnterpriseMapper;
import com.internship.mapper.EnterpriseUserMapper;
import com.internship.service.EnterpriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnterpriseServiceImpl extends ServiceImpl<EnterpriseMapper, Enterprise> implements EnterpriseService {
    @Autowired
    private EnterpriseUserMapper enterpriseUserMapper;
    
    @Override
    public Page<EnterpriseDTO> pageEnterprises(PageDTO dto) {
        Page<Enterprise> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapper<Enterprise> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(dto.getKeyword())) {
            wrapper.like(Enterprise::getEnterpriseName, dto.getKeyword())
                   .or().like(Enterprise::getContactPerson, dto.getKeyword());
        }
        if (dto.getStatus() != null) {
            wrapper.eq(Enterprise::getAuditStatus, dto.getStatus());
        }
        wrapper.orderByDesc(Enterprise::getCreateTime);
        
        Page<Enterprise> enterprisePage = page(page, wrapper);
        Page<EnterpriseDTO> resultPage = new Page<>(enterprisePage.getCurrent(), enterprisePage.getSize(), enterprisePage.getTotal());
        List<EnterpriseDTO> records = enterprisePage.getRecords().stream()
            .map(e -> BeanUtil.copyProperties(e, EnterpriseDTO.class))
            .toList();
        resultPage.setRecords(records);
        return resultPage;
    }
    
    @Override
    public EnterpriseDTO getEnterpriseDetail(Long id) {
        Enterprise enterprise = getById(id);
        if (enterprise == null) {
            throw new RuntimeException("企业不存在");
        }
        return BeanUtil.copyProperties(enterprise, EnterpriseDTO.class);
    }
    
    @Override
    public void auditEnterprise(Long id, Integer auditStatus, String auditRemark, Long auditBy) {
        Enterprise enterprise = getById(id);
        if (enterprise == null) {
            throw new RuntimeException("企业不存在");
        }
        enterprise.setAuditStatus(auditStatus);
        enterprise.setAuditRemark(auditRemark);
        enterprise.setAuditBy(auditBy);
        updateById(enterprise);
    }
    
    @Override
    public Enterprise getCurrentEnterprise(Long userId) {
        EnterpriseUser enterpriseUser = enterpriseUserMapper.selectOne(
            new LambdaQueryWrapper<EnterpriseUser>().eq(EnterpriseUser::getUserId, userId));
        if (enterpriseUser == null) {
            return null;
        }
        return getById(enterpriseUser.getEnterpriseId());
    }
}
