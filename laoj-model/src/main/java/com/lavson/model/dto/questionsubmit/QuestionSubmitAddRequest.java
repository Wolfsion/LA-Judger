package com.lavson.model.dto.questionsubmit;

import lombok.Data;

import java.io.Serializable;

/**
 * 题目提交添加请求
 *
 * @author LA
 * @version 1.0
 * 2024/5/7 - 20:14
 */
@Data
public class QuestionSubmitAddRequest implements Serializable {

    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;

    /**
     * 题目 id
     */
    private Long questionId;

    private static final long serialVersionUID = 1L;
}
