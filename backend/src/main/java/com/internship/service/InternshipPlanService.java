package com.internship.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.internship.dto.InternshipPlanDTO;
import com.internship.dto.InternshipPositionDTO;
import com.internship.dto.PageDTO;
import com.internship.entity.InternshipPlan;

public interface InternshipPlanService extends IService<InternshipPlan> {
    Page<InternshipPlanDTO> pagePlans(PageDTO dto);
    void createPlan(InternshipPlanDTO dto, Long createBy);
    void updatePlanStatus(Long id, Integer status);
}
