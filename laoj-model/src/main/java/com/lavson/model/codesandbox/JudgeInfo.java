package com.lavson.model.codesandbox;

import com.lavson.model.enums.JudgeResultEnum;
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
     * 使用内存（KB）
     */
    private Long memory;

    /**
     * 执行时间（ms）
     */
    private Long time;

    /**
     * 程序执行信息
     */
    private JudgeResultEnum judge;
}
