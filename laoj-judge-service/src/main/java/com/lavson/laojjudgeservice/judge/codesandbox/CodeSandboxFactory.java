package com.lavson.laojjudgeservice.judge.codesandbox;

import com.lavson.laojjudgeservice.judge.codesandbox.impl.ExampleCodeSandbox;
import com.lavson.laojjudgeservice.judge.codesandbox.impl.RemoteCodeSandbox;

/**
 * todo
 *
 * @author LA
 * @version 1.0
 * 2024/5/8 - 16:49
 */
public class CodeSandboxFactory {

    /**
     * 创建代码沙箱示例
     *
     * @param type 沙箱类型
     * @return
     */
    public static CodeSandbox newInstance(String type) {
        switch (type) {
            case "example":
                return new ExampleCodeSandbox();
            case "remote":
                return new RemoteCodeSandbox();
            default:
                return new ExampleCodeSandbox();
        }
    }
}
