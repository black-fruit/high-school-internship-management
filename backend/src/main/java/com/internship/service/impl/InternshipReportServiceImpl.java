package com.internship.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internship.dto.InternshipReportDTO;
import com.internship.entity.InternshipReport;
import com.internship.mapper.InternshipReportMapper;
import com.internship.service.InternshipReportService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class InternshipReportServiceImpl extends ServiceImpl<InternshipReportMapper, InternshipReport> implements InternshipReportService {
    @Override
    public InternshipReportDTO getReport(Long applicationId) {
        InternshipReport report = getOne(new LambdaQueryWrapper<InternshipReport>()
            .eq(InternshipReport::getApplicationId, applicationId));
        if (report == null) {
            return null;
        }
        return BeanUtil.copyProperties(report, InternshipReportDTO.class);
    }
    
    @Override
    public void submitReport(Long studentId, Long applicationId, String title, String content, String attachment) {
        InternshipReport existing = getOne(new LambdaQueryWrapper<InternshipReport>()
            .eq(InternshipReport::getApplicationId, applicationId));
        
        if (existing != null) {
            existing.setTitle(title);
            existing.setContent(content);
            existing.setAttachment(attachment);
            existing.setSubmitTime(LocalDateTime.now());
            updateById(existing);
        } else {
            InternshipReport report = new InternshipReport();
            report.setStudentId(studentId);
            report.setApplicationId(applicationId);
            report.setTitle(title);
            report.setContent(content);
            report.setAttachment(attachment);
            report.setSubmitTime(LocalDateTime.now());
            save(report);
        }
    }
}
