package com.internship.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.internship.common.Result;
import com.internship.dto.*;
import com.internship.security.LoginUser;
import com.internship.service.InternshipPlanService;
import com.internship.service.InternshipPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internship")
public class InternshipController {
    @Autowired
    private InternshipPlanService planService;
    
    @Autowired
    private InternshipPositionService positionService;
    
    @GetMapping("/plan/page")
    public Result<Page<InternshipPlanDTO>> pagePlans(PageDTO dto) {
        return Result.success(planService.pagePlans(dto));
    }
    
    @PostMapping("/plan")
    public Result<Void> createPlan(@RequestBody InternshipPlanDTO dto) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        planService.createPlan(dto, loginUser.getUser().getId());
        return Result.success();
    }
    
    @PutMapping("/plan/status/{id}")
    public Result<Void> updatePlanStatus(@PathVariable Long id, @RequestParam Integer status) {
        planService.updatePlanStatus(id, status);
        return Result.success();
    }
    
    @GetMapping("/position/page")
    public Result<Page<InternshipPositionDTO>> pagePositions(PageDTO dto) {
        return Result.success(positionService.pagePositions(dto));
    }
    
    @GetMapping("/position/available")
    public Result<Page<InternshipPositionDTO>> pageAvailablePositions(PageDTO dto) {
        return Result.success(positionService.pageAvailablePositions(dto));
    }
    
    @GetMapping("/position/{id}")
    public Result<InternshipPositionDTO> getPosition(@PathVariable Long id) {
        return Result.success(positionService.getPositionDetail(id));
    }
    
    @PostMapping("/position")
    public Result<Void> createPosition(@RequestBody InternshipPositionDTO dto) {
        positionService.createPosition(dto);
        return Result.success();
    }
    
    @PutMapping("/position/status/{id}")
    public Result<Void> updatePositionStatus(@PathVariable Long id, @RequestParam Integer status) {
        positionService.updatePositionStatus(id, status);
        return Result.success();
    }
}
