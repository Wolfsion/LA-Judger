package com.lavson.laojjudgeservice.service;

import com.lavson.laojjudgeservice.judge.strategy.DefaultJudgeStrategy;
import com.lavson.laojjudgeservice.judge.strategy.JavaLanguageJudgeStrategy;
import com.lavson.laojjudgeservice.judge.strategy.JudgeContext;
import com.lavson.laojjudgeservice.judge.strategy.JudgeStrategy;
import com.lavson.model.codesandbox.JudgeInfo;
import com.lavson.model.entity.QuestionSubmit;
import com.lavson.model.enums.CodeLanguageEnum;
import org.springframework.stereotype.Service;

/**
 * todo
 *
 * @author LA
 * @version 1.0
 * 2024/5/8 - 16:43
 */
@Service
public class JudgeManager {

    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if (CodeLanguageEnum.JAVA.getText().equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }

}

