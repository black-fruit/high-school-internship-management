package com.internship.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.internship.entity.InternshipPlan;
import com.internship.entity.InternshipPosition;
import com.internship.mapper.InternshipPlanMapper;
import com.internship.mapper.InternshipPositionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class StatusScheduleTask {
    
    private static final Logger log = LoggerFactory.getLogger(StatusScheduleTask.class);
    
    @Autowired
    private InternshipPlanMapper planMapper;
    
    @Autowired
    private InternshipPositionMapper positionMapper;
    
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateExpiredStatus() {
        log.info("开始执行定时任务：更新过期状态");
        LocalDate today = LocalDate.now();
        
        LambdaQueryWrapper<InternshipPlan> planWrapper = new LambdaQueryWrapper<>();
        planWrapper.eq(InternshipPlan::getStatus, 1)
                   .lt(InternshipPlan::getEndDate, today);
        InternshipPlan planUpdate = new InternshipPlan();
        planUpdate.setStatus(2);
        int planCount = planMapper.update(planUpdate, planWrapper);
        if (planCount > 0) {
            log.info("已自动结束 {} 个实习计划", planCount);
        }
        
        LambdaQueryWrapper<InternshipPosition> positionWrapper = new LambdaQueryWrapper<>();
        positionWrapper.eq(InternshipPosition::getStatus, 1)
                       .lt(InternshipPosition::getEndDate, today);
        InternshipPosition positionUpdate = new InternshipPosition();
        positionUpdate.setStatus(0);
        int positionCount = positionMapper.update(positionUpdate, positionWrapper);
        if (positionCount > 0) {
            log.info("已自动关闭 {} 个过期岗位", positionCount);
        }
        
        log.info("定时任务执行完成");
    }
}
