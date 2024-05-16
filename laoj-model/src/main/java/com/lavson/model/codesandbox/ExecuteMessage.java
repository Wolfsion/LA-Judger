package com.lavson.model.codesandbox;

import com.lavson.model.enums.JudgeResultEnum;
import lombok.Data;

/**
 * 单个测例的执行信息
 *
 * @author LA
 * @version 1.0
 * 2024/5/14 - 20:54
 */
@Data
public class ExecuteMessage {
    /**
     * 程序退出码，必须为ExitCodeEnum.value
     */
    private Integer exitValue;

    /**
     * 正常执行结果输出
     */
    private String output;

    /**
     * 错误执行信息
     */
    private String errorMessage;

    /**
     * 执行耗时(ms)
     */
    private Long time;

    /**
     * 执行占用(KB)
     */
    private Long memory;

    /**
     * 初步判题结果，After Execute类别
     */
    private JudgeResultEnum judge;
}