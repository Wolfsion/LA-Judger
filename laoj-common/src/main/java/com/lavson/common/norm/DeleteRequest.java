package com.lavson.common.norm;

import lombok.Data;

import java.io.Serializable;

/**
 * 删除请求 todo
 *
 * @author LA
 * @version 1.0
 * 2024/5/6 - 21:14
 */
@Data
public class DeleteRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}
