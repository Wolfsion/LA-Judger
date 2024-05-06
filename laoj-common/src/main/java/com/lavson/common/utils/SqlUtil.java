package com.lavson.common.utils;


import org.apache.commons.lang3.StringUtils;

/**
 * SQL工具
 *
 * @author LA
 * @version 1.0
 * 2024/5/6 - 21:04
 */
public class SqlUtil {

    /**
     * 校验排序字段是否合法（防止 SQL 注入）
     *
     * @param sortField
     * @return
     */
    public static boolean validSortField(String sortField) {
        if (StringUtils.isBlank(sortField)) {
            return false;
        }
        return !StringUtils.containsAny(sortField, "=", "(", ")", " ");
    }
}
