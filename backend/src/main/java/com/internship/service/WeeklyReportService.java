package com.internship.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.internship.dto.*;
import com.internship.entity.WeeklyReport;

public interface WeeklyReportService extends IService<WeeklyReport> {
    Page<WeeklyReportDTO> pageReports(PageDTO dto);
    Page<WeeklyReportDTO> pageMyReports(Long studentId, PageDTO dto);
    void submitReport(Long studentId, Long applicationId, Integer weekNumber, String content);
    void commentReport(Long id, Long teacherId, String comment);
}
