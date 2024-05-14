package com.lavson.laojcodesandbox.service;

import com.lavson.model.codesandbox.ExecuteCodeRequest;
import com.lavson.model.codesandbox.ExecuteCodeResponse;

import java.io.IOException;

/**
 * todo
 *
 * @author LA
 * @version 1.0
 * 2024/5/14 - 20:32
 */
public interface CodeSandbox {

    /**
     * 执行代码
     *
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) throws IOException;
}
