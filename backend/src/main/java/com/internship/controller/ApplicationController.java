package com.internship.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.internship.common.Result;
import com.internship.dto.*;
import com.internship.entity.Enterprise;
import com.internship.security.LoginUser;
import com.internship.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/application")
public class ApplicationController {
    @Autowired
    private InternshipApplicationService applicationService;
    
    @Autowired
    private WeeklyReportService weeklyReportService;
    
    @Autowired
    private InternshipReportService internshipReportService;
    
    @Autowired
    private InternshipScoreService internshipScoreService;
    
    @Autowired
    private EnterpriseService enterpriseService;
    
    @GetMapping("/page")
    public Result<Page<InternshipApplicationDTO>> pageApplications(PageDTO dto) {
        return Result.success(applicationService.pageApplications(dto));
    }
    
    @GetMapping("/my")
    public Result<Page<InternshipApplicationDTO>> pageMyApplications(PageDTO dto) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Result.success(applicationService.pageMyApplications(loginUser.getUser().getId(), dto));
    }
    
    @GetMapping("/enterprise")
    public Result<Page<InternshipApplicationDTO>> pageEnterpriseApplications(PageDTO dto) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Enterprise enterprise = enterpriseService.getCurrentEnterprise(loginUser.getUser().getId());
        if (enterprise == null) {
            return Result.error("您不是企业用户");
        }
        return Result.success(applicationService.pageEnterpriseApplications(enterprise.getId(), dto));
    }
    
    @GetMapping("/teacher")
    public Result<Page<InternshipApplicationDTO>> pageTeacherApplications(PageDTO dto) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Result.success(applicationService.pageTeacherApplications(loginUser.getUser().getId(), dto));
    }
    
    @PostMapping("/apply")
    public Result<Void> apply(@RequestParam Long positionId, @RequestParam String resume) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        applicationService.apply(loginUser.getUser().getId(), positionId, resume);
        return Result.success();
    }
    
    @PostMapping("/approve/{id}")
    public Result<Void> approve(@PathVariable Long id) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        applicationService.approve(id, loginUser.getUser().getId(), loginUser.getUser().getRealName());
        return Result.success();
    }
    
    @PostMapping("/reject/{id}")
    public Result<Void> reject(@PathVariable Long id, @RequestParam String rejectReason) {
        applicationService.reject(id, rejectReason);
        return Result.success();
    }
    
    @GetMapping("/weekly/page")
    public Result<Page<WeeklyReportDTO>> pageWeeklyReports(PageDTO dto) {
        return Result.success(weeklyReportService.pageReports(dto));
    }
    
    @GetMapping("/weekly/my")
    public Result<Page<WeeklyReportDTO>> pageMyWeeklyReports(PageDTO dto) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Result.success(weeklyReportService.pageMyReports(loginUser.getUser().getId(), dto));
    }
    
    @PostMapping("/weekly")
    public Result<Void> submitWeeklyReport(@RequestParam Long applicationId, @RequestParam Integer weekNumber, 
                                           @RequestParam String content) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        weeklyReportService.submitReport(loginUser.getUser().getId(), applicationId, weekNumber, content);
        return Result.success();
    }
    
    @PostMapping("/weekly/comment/{id}")
    public Result<Void> commentWeeklyReport(@PathVariable Long id, @RequestParam String comment) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        weeklyReportService.commentReport(id, loginUser.getUser().getId(), comment);
        return Result.success();
    }
    
    @GetMapping("/report")
    public Result<InternshipReportDTO> getInternshipReport(@RequestParam Long applicationId) {
        return Result.success(internshipReportService.getReport(applicationId));
    }
    
    @PostMapping("/report")
    public Result<Void> submitInternshipReport(@RequestParam Long applicationId, @RequestParam String title,
                                               @RequestParam String content, @RequestParam(required = false) String attachment) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        internshipReportService.submitReport(loginUser.getUser().getId(), applicationId, title, content, attachment);
        return Result.success();
    }
    
    @GetMapping("/score")
    public Result<InternshipScoreDTO> getScore(@RequestParam Long applicationId) {
        return Result.success(internshipScoreService.getScore(applicationId));
    }
    
    @PostMapping("/score/teacher")
    public Result<Void> teacherScore(@RequestParam Long applicationId, @RequestParam Double score,
                                     @RequestParam(required = false) String comment) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        internshipScoreService.teacherScore(applicationId, loginUser.getUser().getId(), score, comment);
        return Result.success();
    }
    
    @PostMapping("/score/enterprise")
    public Result<Void> enterpriseScore(@RequestParam Long applicationId, @RequestParam Double score,
                                        @RequestParam(required = false) String comment) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        internshipScoreService.enterpriseScore(applicationId, loginUser.getUser().getId(), score, comment);
        return Result.success();
    }
}
