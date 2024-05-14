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

    private List<String> inputList;

    private String code;

    private String language;

    private JudgeConfig config;
}
