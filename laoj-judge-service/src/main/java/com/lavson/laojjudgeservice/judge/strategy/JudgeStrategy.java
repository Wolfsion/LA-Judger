package com.lavson.laojjudgeservice.judge.strategy;

import com.lavson.model.codesandbox.JudgeInfo;

/**
 * todo
 *
 * @author LA
 * @version 1.0
 * 2024/5/8 - 16:44
 */
public interface JudgeStrategy {

    /**
     * 执行判题
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext);
}
