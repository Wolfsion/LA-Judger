package com.lavson.laojcodesandbox.service;

import com.lavson.common.exception.BusinessException;
import com.lavson.common.norm.ErrorCode;
import com.lavson.laojcodesandbox.service.java.JavaDockerBox;
import com.lavson.model.codesandbox.ExecuteCodeRequest;
import com.lavson.model.codesandbox.ExecuteCodeResponse;
import com.lavson.model.enums.CodeLanguageEnum;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * todo
 *
 * @author LA
 * @version 1.0
 * 2024/5/14 - 20:44
 */

@Service
@Data
@RequiredArgsConstructor
public class CodeBoxManager {

    private final JavaDockerBox javaDockerBox;

    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        if (CodeLanguageEnum.JAVA.getText().equals(executeCodeRequest.getLanguage())) {
            return javaDockerBox.executeCode(executeCodeRequest);
        } else {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编程语言错误");
        }
    }
}
