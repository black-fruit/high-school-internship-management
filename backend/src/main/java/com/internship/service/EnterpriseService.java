package com.internship.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.internship.dto.EnterpriseDTO;
import com.internship.dto.PageDTO;
import com.internship.entity.Enterprise;

public interface EnterpriseService extends IService<Enterprise> {
    Page<EnterpriseDTO> pageEnterprises(PageDTO dto);
    EnterpriseDTO getEnterpriseDetail(Long id);
    void auditEnterprise(Long id, Integer auditStatus, String auditRemark, Long auditBy);
    Enterprise getCurrentEnterprise(Long userId);
}
