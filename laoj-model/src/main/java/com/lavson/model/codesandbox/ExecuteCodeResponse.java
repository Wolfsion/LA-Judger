package com.lavson.model.codesandbox;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * todo
 *
 * @author LA
 * @version 1.0
 * 2024/5/7 - 20:05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteCodeResponse {
    /**
     * 执行输出结果
     */
    private List<String> outputList;

    /**
     * 接口信息，留置待用，冗余设计
     */
    private String message;

    /**
     * 执行状态，所有测例执行状态，必须为JudgeStatusEnum.value
     */
    private Integer status;

    /**
     * 判题信息
     */
    private List<JudgeInfo> judgeInfos;
}
