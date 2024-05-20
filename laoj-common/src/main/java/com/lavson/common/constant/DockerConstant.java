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
    String DOCKER_IMAGE_NAME = "openjdk:8-alpine";
    Long MEMORY_SWAP = 0L;
    Long CPU_COUNT = 1L;
    String DOCKER_MOUNT_DIR = "/app";
    String DOCKER_MOUNT_IO_DIR = "/questionIO";
    String QUESTION_IS_DIR = System.getProperty("user.dir") + File.separator + "/questionIO";

    // "--read-only"
    String[] JAVA_EXECUTE = new String[]{"java", "-Xmx128m", "-cp", DOCKER_MOUNT_DIR, "Main", "<"};

    String DOCKER_TCP = "tcp://";

    String[] TEST_EXECUTE = new String[]{"echo"};
    String[] TEST_LONG_EXECUTE = new String[]{"sleep", "2"};

//    String[] REF_CMD = new String[] {
//            "sh",
//            "-c",
//            "echo '"
//                    + config.getCode()
//                    + "' > Main.java "
//                    + "&& echo '"
//                    + config.getInput()
//                    + "' > input.in "
//                    + "&& javac Main.java"
//                    + " && /usr/bin/time -f \"%U:%X\" -o /home/consume.out java Main < input.in"
//    };
}
