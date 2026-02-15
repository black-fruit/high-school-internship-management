package com.internship.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.internship.dto.InternshipPositionDTO;
import com.internship.dto.PageDTO;
import com.internship.entity.InternshipPosition;

public interface InternshipPositionService extends IService<InternshipPosition> {
    Page<InternshipPositionDTO> pagePositions(PageDTO dto);
    Page<InternshipPositionDTO> pageAvailablePositions(PageDTO dto);
    InternshipPositionDTO getPositionDetail(Long id);
    void createPosition(InternshipPositionDTO dto);
    void updatePositionStatus(Long id, Integer status);
}
