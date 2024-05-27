package com.lavson.laojjudgeservice.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.lavson.model.codesandbox.JudgeInfo;
import com.lavson.model.entity.JudgeCase;
import com.lavson.model.entity.JudgeConfig;
import com.lavson.model.entity.Question;
import com.lavson.model.enums.JudgeResultEnum;

import java.util.List;
import java.util.Optional;

/**
 * todo
 *
 * @author LA
 * @version 1.0
 * 2024/5/8 - 16:46
 */
public class JavaLanguageJudgeStrategy extends DefaultJudgeStrategy implements JudgeStrategy{

    /**
     * 执行判题
     * @param judgeContext
     * @return
     */
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        return super.doJudge(judgeContext);
    }
}

