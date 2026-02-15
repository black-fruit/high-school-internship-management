package com.internship.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.internship.dto.InternshipScoreDTO;
import com.internship.entity.InternshipScore;

public interface InternshipScoreService extends IService<InternshipScore> {
    InternshipScoreDTO getScore(Long applicationId);
    void teacherScore(Long applicationId, Long teacherId, Double score, String comment);
    void enterpriseScore(Long applicationId, Long enterpriseUserId, Double score, String comment);
}
