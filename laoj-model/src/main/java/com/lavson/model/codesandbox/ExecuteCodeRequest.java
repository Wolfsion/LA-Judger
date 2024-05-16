package com.lavson.model.codesandbox;

import com.lavson.model.entity.JudgeConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 代码执行请求
 *
 * @author LA
 * @version 1.0
 * 2024/5/7 - 20:04
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteCodeRequest {
    /**
     * 输入测例
     */
    private List<String> inputList;

    /**
     * 提交代码
     */
    private String code;

    /**
     * 编程语言，必须为CodeLanguageEnum.value
     */
    private String language;

    /**
     * 题目配置
     */
    private JudgeConfig config;

    /**
     * 当前要执行的测例开始索引
     */
    private Integer offset;
}
