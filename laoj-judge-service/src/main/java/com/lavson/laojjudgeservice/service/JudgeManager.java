package com.lavson.laojjudgeservice.service;

import com.lavson.laojjudgeservice.judge.strategy.DefaultJudgeStrategy;
import com.lavson.laojjudgeservice.judge.strategy.JavaLanguageJudgeStrategy;
import com.lavson.laojjudgeservice.judge.strategy.JudgeContext;
import com.lavson.laojjudgeservice.judge.strategy.JudgeStrategy;
import com.lavson.model.codesandbox.JudgeInfo;
import com.lavson.model.entity.QuestionSubmit;
import com.lavson.model.enums.CodeLanguageEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * todo
 *
 * @author LA
 * @version 1.0
 * 2024/5/8 - 16:43
 */
@Service
@Slf4j
public class JudgeManager {

    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        String language = judgeContext.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if (CodeLanguageEnum.JAVA.getText().equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        } else {
            log.error("Not support language.");
        }
        return judgeStrategy.doJudge(judgeContext);
    }

}

