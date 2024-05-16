package com.lavson.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * todo
 *
 * @author LA
 * @version 1.0
 * 2024/5/16 - 17:46
 */
public enum ExitCodeEnum {

    SUCCESS("Success", 0),
    UNKNOWN_ERROR("Unknown error occurred", 1),
    INVALID_ARGUMENTS("Invalid command line arguments", 2),
    SANDBOX_ERROR("Code Sandbox error", 3);


    private final String text;

    private final Integer value;

    ExitCodeEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<Integer> getValues() {
        return Arrays.stream(ExitCodeEnum.values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static ExitCodeEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (ExitCodeEnum anEnum : ExitCodeEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public Integer getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}

