package com.lavson.laojjudgeservice.judge.strategy;

import com.lavson.model.codesandbox.JudgeInfo;
import com.lavson.model.entity.JudgeCase;
import com.lavson.model.entity.Question;
import com.lavson.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * todo
 *
 * @author LA
 * @version 1.0
 * 2024/5/8 - 16:44
 */
@Data
public class JudgeContext {

    private List<JudgeInfo> judgeInfos;

    private List<String> inputList;

    private List<String> outputList;

    private List<JudgeCase> judgeCaseList;

    private Question question;

    private QuestionSubmit questionSubmit;

}
