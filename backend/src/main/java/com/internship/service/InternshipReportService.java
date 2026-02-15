package com.internship.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.internship.dto.InternshipReportDTO;
import com.internship.entity.InternshipReport;

public interface InternshipReportService extends IService<InternshipReport> {
    InternshipReportDTO getReport(Long applicationId);
    void submitReport(Long studentId, Long applicationId, String title, String content, String attachment);
}
