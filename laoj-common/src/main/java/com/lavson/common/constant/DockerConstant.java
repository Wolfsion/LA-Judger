package com.lavson.common.constant;

/**
 * todo
 *
 * @author LA
 * @version 1.0
 * 2024/5/16 - 19:34
 */
public interface DockerConstant {
    String DOCKER_IMAGE_NAME = "openjdk:8-alpine";
    Long MEMORY_SWAP = 0L;
    Long CPU_COUNT = 1L;
    String DOCKER_MOUNT_DIR = "/app";

    String[] JAVA_EXECUTE = new String[]{"java", "-cp", DOCKER_MOUNT_DIR, "Main", "--read-only"};

    String DOCKER_TCP = "tcp://";

    String[] JAVA_TEST_EXECUTE = new String[]{"echo", "hello"};
}
