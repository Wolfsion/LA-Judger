package com.lavson.laojjudgeservice.service;

import com.lavson.model.entity.QuestionSubmit;

/**
 * todo
 *
 * @author LA
 * @version 1.0
 * 2024/5/8 - 16:41
 */
public interface JudgeService {

    /**
     * 判题
     * @param questionSubmitId
     * @return
     */
    QuestionSubmit doJudge(long questionSubmitId);
}
