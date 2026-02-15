package com.internship.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.internship.dto.*;
import com.internship.entity.InternshipApplication;

public interface InternshipApplicationService extends IService<InternshipApplication> {
    Page<InternshipApplicationDTO> pageApplications(PageDTO dto);
    Page<InternshipApplicationDTO> pageMyApplications(Long studentId, PageDTO dto);
    Page<InternshipApplicationDTO> pageEnterpriseApplications(Long enterpriseId, PageDTO dto);
    Page<InternshipApplicationDTO> pageTeacherApplications(Long teacherId, PageDTO dto);
    void apply(Long studentId, Long positionId, String resume);
    void approve(Long id, Long teacherId, String teacherName);
    void reject(Long id, String rejectReason);
    InternshipApplication getByStudentAndPosition(Long studentId, Long positionId);
}
