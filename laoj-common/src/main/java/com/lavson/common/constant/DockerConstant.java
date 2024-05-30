package com.lavson.common.constant;

import java.io.File;

/**
 * todo
 *
 * @author LA
 * @version 1.0
 * 2024/5/16 - 19:34
 */
public interface DockerConstant {
    String DOCKER_TCP = "tcp://";
    String DOCKER_IMAGE_NAME = "openjdk:8-alpine";
    Long MEMORY_SWAP = 0L;
    Long CPU_COUNT = 1L;
    String DOCKER_MOUNT_DIR = "/app";
    String CODE_DIR = System.getProperty("user.dir") + File.separator +
            "laoj-code-sandbox" + File.separator +
            "/tmpCode";
    String DOCKER_MOUNT_IO_DIR = "/questionIO";
    String QUESTION_IS_DIR = System.getProperty("user.dir") + File.separator +
                                "laoj-code-sandbox" + File.separator +
                                "/questionIO";
    // "--read-only"
    String[] JAVA_EXECUTE = new String[]{"sh", "-c", "java -Xmx128m -cp /app Main"};
    String TIME_MEMORY_MARK = "_TM_#";
    String[] TIME_MEMORY_JAVA_EXECUTE = new String[]{"sh", "-c", "/usr/bin/time -f " +
                                                "\"" + TIME_MEMORY_MARK + "%U:%M\" " +
                                                "java -Xmx128m -cp /app"};
    String[] TEST_EXECUTE = new String[]{"echo"};
    String[] TEST_LONG_EXECUTE = new String[]{"sh", "-c", "echo hello && sleep 1"};
    Integer METRIC_LEN = 2;
    Integer INDEX_TIME = 0;
    Integer INDEX_MEMORY = 1;
    String TIME_MEMORY_REGEX = TIME_MEMORY_MARK+"(\\d+\\.\\d+):(\\d+)";
}
