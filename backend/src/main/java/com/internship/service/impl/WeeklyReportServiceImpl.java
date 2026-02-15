package com.internship.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internship.dto.*;
import com.internship.entity.WeeklyReport;
import com.internship.mapper.WeeklyReportMapper;
import com.internship.service.WeeklyReportService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WeeklyReportServiceImpl extends ServiceImpl<WeeklyReportMapper, WeeklyReport> implements WeeklyReportService {
    @Override
    public Page<WeeklyReportDTO> pageReports(PageDTO dto) {
        Page<WeeklyReport> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapper<WeeklyReport> wrapper = new LambdaQueryWrapper<>();
        if (dto.getApplicationId() != null) {
            wrapper.eq(WeeklyReport::getApplicationId, dto.getApplicationId());
        }
        if (dto.getStudentId() != null) {
            wrapper.eq(WeeklyReport::getStudentId, dto.getStudentId());
        }
        wrapper.orderByDesc(WeeklyReport::getCreateTime);
        
        Page<WeeklyReport> reportPage = page(page, wrapper);
        Page<WeeklyReportDTO> resultPage = new Page<>(reportPage.getCurrent(), reportPage.getSize(), reportPage.getTotal());
        List<WeeklyReportDTO> records = reportPage.getRecords().stream()
            .map(r -> BeanUtil.copyProperties(r, WeeklyReportDTO.class))
            .toList();
        resultPage.setRecords(records);
        return resultPage;
    }
    
    @Override
    public Page<WeeklyReportDTO> pageMyReports(Long studentId, PageDTO dto) {
        Page<WeeklyReport> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapper<WeeklyReport> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WeeklyReport::getStudentId, studentId);
        if (dto.getApplicationId() != null) {
            wrapper.eq(WeeklyReport::getApplicationId, dto.getApplicationId());
        }
        wrapper.orderByDesc(WeeklyReport::getCreateTime);
        
        Page<WeeklyReport> reportPage = page(page, wrapper);
        Page<WeeklyReportDTO> resultPage = new Page<>(reportPage.getCurrent(), reportPage.getSize(), reportPage.getTotal());
        List<WeeklyReportDTO> records = reportPage.getRecords().stream()
            .map(r -> BeanUtil.copyProperties(r, WeeklyReportDTO.class))
            .toList();
        resultPage.setRecords(records);
        return resultPage;
    }
    
    @Override
    public void submitReport(Long studentId, Long applicationId, Integer weekNumber, String content) {
        WeeklyReport report = new WeeklyReport();
        report.setStudentId(studentId);
        report.setApplicationId(applicationId);
        report.setWeekNumber(weekNumber);
        report.setContent(content);
        save(report);
    }
    
    @Override
    public void commentReport(Long id, Long teacherId, String comment) {
        WeeklyReport report = getById(id);
        if (report == null) {
            throw new RuntimeException("周报不存在");
        }
        report.setTeacherComment(comment);
        report.setTeacherId(teacherId);
        report.setCommentTime(LocalDateTime.now());
        updateById(report);
    }
}
