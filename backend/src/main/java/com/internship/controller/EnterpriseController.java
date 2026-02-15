package com.internship.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.internship.common.Result;
import com.internship.dto.*;
import com.internship.entity.Enterprise;
import com.internship.security.LoginUser;
import com.internship.service.EnterpriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/enterprise")
public class EnterpriseController {
    @Autowired
    private EnterpriseService enterpriseService;
    
    @GetMapping("/page")
    public Result<Page<EnterpriseDTO>> pageEnterprises(PageDTO dto) {
        return Result.success(enterpriseService.pageEnterprises(dto));
    }
    
    @GetMapping("/{id}")
    public Result<EnterpriseDTO> getEnterprise(@PathVariable Long id) {
        return Result.success(enterpriseService.getEnterpriseDetail(id));
    }
    
    @PostMapping("/audit")
    public Result<Void> auditEnterprise(@RequestParam Long id, @RequestParam Integer auditStatus, 
                                        @RequestParam(required = false) String auditRemark) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        enterpriseService.auditEnterprise(id, auditStatus, auditRemark, loginUser.getUser().getId());
        return Result.success();
    }
    
    @GetMapping("/current")
    public Result<Enterprise> getCurrentEnterprise() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Result.success(enterpriseService.getCurrentEnterprise(loginUser.getUser().getId()));
    }
    
    @PostMapping
    public Result<Void> createEnterprise(@RequestBody EnterpriseDTO dto) {
        Enterprise enterprise = new Enterprise();
        enterprise.setEnterpriseName(dto.getEnterpriseName());
        enterprise.setCreditCode(dto.getCreditCode());
        enterprise.setContactPerson(dto.getContactPerson());
        enterprise.setContactPhone(dto.getContactPhone());
        enterprise.setEmail(dto.getEmail());
        enterprise.setAddress(dto.getAddress());
        enterprise.setDescription(dto.getDescription());
        enterprise.setAuditStatus(0);
        enterpriseService.save(enterprise);
        return Result.success();
    }
    
    @PutMapping
    public Result<Void> updateEnterprise(@RequestBody EnterpriseDTO dto) {
        Enterprise enterprise = enterpriseService.getById(dto.getId());
        if (enterprise != null) {
            enterprise.setEnterpriseName(dto.getEnterpriseName());
            enterprise.setCreditCode(dto.getCreditCode());
            enterprise.setContactPerson(dto.getContactPerson());
            enterprise.setContactPhone(dto.getContactPhone());
            enterprise.setEmail(dto.getEmail());
            enterprise.setAddress(dto.getAddress());
            enterprise.setDescription(dto.getDescription());
            enterpriseService.updateById(enterprise);
        }
        return Result.success();
    }
}
