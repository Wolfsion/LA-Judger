package com.lavson.model.codesandbox;

import lombok.Data;

/**
 * todo
 *
 * @author LA
 * @version 1.0
 * 2024/5/14 - 20:54
 */
@Data
public class ExecuteMessage {

    private Integer exitValue;

    private String message;

    private String errorMessage;

    private Long time;

    private Long memory;
}