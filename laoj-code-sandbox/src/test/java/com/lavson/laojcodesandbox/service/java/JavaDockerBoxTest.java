package com.lavson.laojcodesandbox.service.java;

import cn.hutool.core.io.resource.ResourceUtil;
import com.github.dockerjava.api.DockerClient;
import com.lavson.common.constant.SandBoxConstant;
import com.lavson.model.codesandbox.ExecuteCodeRequest;
import com.lavson.model.codesandbox.ExecuteCodeResponse;
import com.lavson.model.entity.JudgeConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

import javax.print.Doc;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * todo
 *
 * @author LA
 * @version 1.0
 * 2024/5/16 - 19:08
 */

@SpringBootTest
class JavaDockerBoxTest {
    @Autowired
    JavaDockerBox javaDockerBox;
    @Autowired
    DockerClient dockerClient;

    @Test
    public void testRunWithFile() {
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setInputList(Arrays.asList("/questionIO/1790020640590295042/1.in",
                "/questionIO/1790020640590295042/2.in"));
        String code = ResourceUtil.readStr("testCode/java/Main.java", StandardCharsets.UTF_8);
        executeCodeRequest.setCode(code);
        executeCodeRequest.setLanguage("java");
        executeCodeRequest.setConfig(new JudgeConfig(2L, SandBoxConstant.DEFAULT_MEMORY_LIMIT, 0L));
        ExecuteCodeResponse executeCodeResponse = javaDockerBox.executeCode(executeCodeRequest);
        System.out.println(executeCodeResponse);
    }

    @Test
    public void testRunWithStream() {
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setInputList(Arrays.asList("1 2\n", "100 200\n"));
        String code = ResourceUtil.readStr("testCode/java/Main.java", StandardCharsets.UTF_8);
        executeCodeRequest.setCode(code);
        executeCodeRequest.setLanguage("java");
        executeCodeRequest.setConfig(new JudgeConfig(2000L, SandBoxConstant.DEFAULT_MEMORY_LIMIT, 0L));
        ExecuteCodeResponse executeCodeResponse = javaDockerBox.executeCode(executeCodeRequest);
        System.out.println(executeCodeResponse);
    }

    @Test
    public void isSame() {
        System.out.println("Test");
    }
}