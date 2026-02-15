package com.internship.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internship.dto.InternshipScoreDTO;
import com.internship.entity.InternshipApplication;
import com.internship.entity.InternshipScore;
import com.internship.mapper.InternshipApplicationMapper;
import com.internship.mapper.InternshipScoreMapper;
import com.internship.service.InternshipScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InternshipScoreServiceImpl extends ServiceImpl<InternshipScoreMapper, InternshipScore> implements InternshipScoreService {
    
    @Autowired
    private InternshipApplicationMapper applicationMapper;
    
    @Override
    public InternshipScoreDTO getScore(Long applicationId) {
        InternshipScore score = getOne(new LambdaQueryWrapper<InternshipScore>()
            .eq(InternshipScore::getApplicationId, applicationId));
        if (score == null) {
            return null;
        }
        return BeanUtil.copyProperties(score, InternshipScoreDTO.class);
    }
    
    @Override
    public void teacherScore(Long applicationId, Long teacherId, Double score, String comment) {
        InternshipScore existing = getOne(new LambdaQueryWrapper<InternshipScore>()
            .eq(InternshipScore::getApplicationId, applicationId));
        
        if (existing != null) {
            existing.setTeacherScore(score);
            existing.setTeacherComment(comment);
            existing.setTeacherId(teacherId);
            calculateTotalScore(existing);
            updateById(existing);
        } else {
            InternshipApplication application = applicationMapper.selectById(applicationId);
            InternshipScore newScore = new InternshipScore();
            newScore.setApplicationId(applicationId);
            newScore.setStudentId(application.getStudentId());
            newScore.setStudentName(application.getStudentName());
            newScore.setTeacherScore(score);
            newScore.setTeacherComment(comment);
            newScore.setTeacherId(teacherId);
            save(newScore);
        }
    }
    
    @Override
    public void enterpriseScore(Long applicationId, Long enterpriseUserId, Double score, String comment) {
        InternshipScore existing = getOne(new LambdaQueryWrapper<InternshipScore>()
            .eq(InternshipScore::getApplicationId, applicationId));
        
        if (existing != null) {
            existing.setEnterpriseScore(score);
            existing.setEnterpriseComment(comment);
            existing.setEnterpriseUserId(enterpriseUserId);
            calculateTotalScore(existing);
            updateById(existing);
        } else {
            InternshipApplication application = applicationMapper.selectById(applicationId);
            InternshipScore newScore = new InternshipScore();
            newScore.setApplicationId(applicationId);
            newScore.setStudentId(application.getStudentId());
            newScore.setStudentName(application.getStudentName());
            newScore.setEnterpriseScore(score);
            newScore.setEnterpriseComment(comment);
            newScore.setEnterpriseUserId(enterpriseUserId);
            save(newScore);
        }
    }
    
    private void calculateTotalScore(InternshipScore score) {
        if (score.getTeacherScore() != null && score.getEnterpriseScore() != null) {
            score.setTotalScore((score.getTeacherScore() + score.getEnterpriseScore()) / 2);
        }
    }
}
