package com.lavson.model.entity;

import lombok.Data;

/**
 * 判题配置
 *
 * @author LA
 * @version 1.0
 * 2024/5/7 - 20:15
 */
@Data
public class JudgeConfig {

    /**
     * 时间限制（ms）
     */
    private Long timeLimit;

    /**
     * 内存限制（KB）
     */
    private Long memoryLimit;

    /**
     * 堆栈限制（KB）
     */
    private Long stackLimit;
}

