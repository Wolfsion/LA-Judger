package com.lavson.laojjudgeservice.judge.codesandbox.impl;

import com.lavson.laojjudgeservice.judge.codesandbox.CodeSandbox;
import com.lavson.model.codesandbox.ExecuteCodeRequest;
import com.lavson.model.codesandbox.ExecuteCodeResponse;
import com.lavson.model.codesandbox.JudgeInfo;
import com.lavson.model.enums.JudgeResultEnum;
import com.lavson.model.enums.JudgeStatusEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

/**
 * todo
 *
 * @author LA
 * @version 1.0
 * 2024/5/8 - 16:50
 */
@Slf4j
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("测试执行成功");
        executeCodeResponse.setStatus(JudgeStatusEnum.SUCCEED.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setJudge(JudgeResultEnum.ACCEPTED);
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(100L);
        executeCodeResponse.setJudgeInfos(Collections.singletonList(judgeInfo));
        return executeCodeResponse;
    }
}
