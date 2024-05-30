package com.lavson.common.constant;

import java.io.File;

/**
 * todo
 *
 * @author LA
 * @version 1.0
 * 2024/5/16 - 16:59
 */
public interface SandBoxConstant {
    String READ_SUB_DIR = "C:\\code\\laoj-code-sandbox";
    String AUTH_REQUEST_HEADER = "auth";
    String AUTH_REQUEST_SECRET = "secretKey";
    String GLOBAL_CODE_DIR_NAME = "laoj-code-sandbox" + File.separator + "tmpCode";
    String GLOBAL_JAVA_CLASS_NAME = "Main.java";
    String GLOBAL_JAVA_MAIN = " Main";
    Long DEFAULT_TIME_LIMIT = 2000L;
    Long DEFAULT_MEMORY_LIMIT = 128*1024L;
    String BLANK_ERROR_MESSAGE = "[]";
}
