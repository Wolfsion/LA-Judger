package com.lavson.model.codesandbox;

import lombok.Data;

/**
 * 判题配置及判题结果信息
 *
 * @author LA
 * @version 1.0
 * 2024/5/7 - 20:05
 */
@Data
public class JudgeInfo {

    /**
     * 内存限制（KB）
     */
    private Long memory;

    /**
     * 时间限制（ms）
     */
    private Long time;

    /**
     * 程序执行信息
     */
    private String message;
}
