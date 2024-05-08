package com.lavson.laojjudgeservice.judge.codesandbox;

import com.lavson.model.codesandbox.ExecuteCodeRequest;
import com.lavson.model.codesandbox.ExecuteCodeResponse;

/**
 * todo
 *
 * @author LA
 * @version 1.0
 * 2024/5/8 - 16:48
 */
public interface CodeSandbox {

    /**
     * 执行代码
     *
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
