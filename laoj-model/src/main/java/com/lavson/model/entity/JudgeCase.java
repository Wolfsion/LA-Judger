package com.lavson.model.entity;

import lombok.Data;

/**
 * 判题侧例
 *
 * @author LA
 * @version 1.0
 * 2024/5/7 - 20:16
 */
@Data
public class JudgeCase {

    /**
     * 输入用例
     */
    private String input;

    /**
     * 输出用例
     */
    private String output;
}
