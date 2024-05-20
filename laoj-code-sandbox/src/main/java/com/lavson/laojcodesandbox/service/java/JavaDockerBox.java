package com.lavson.laojcodesandbox.service.java;

import cn.hutool.core.util.ArrayUtil;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallbackTemplate;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import com.lavson.common.constant.DockerConstant;
import com.lavson.laojcodesandbox.dockerpool.DockerClientPool;
import com.lavson.model.codesandbox.ExecuteMessage;
import com.lavson.model.entity.JudgeConfig;
import com.lavson.model.enums.JudgeResultEnum;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * todo
 *
 * @author LA
 * @version 1.0
 * 2024/5/14 - 20:31
 */
@Slf4j
public class JavaDockerBox extends JavaCodeBoxTemplate{

    /**
     * 3、创建容器，把文件复制到容器内
     * @param userCodeFile
     * @param inputList
     * @return
     */
    @Override
    public List<ExecuteMessage> runFile(File userCodeFile, List<String> inputList, JudgeConfig config) {
        String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();

        // todo: pool, parameter: memory_limit
        // 容器池获取客户端
        DockerClient dockerClient = DockerClientPool.dockerClient();

        // 创建容器
        CreateContainerCmd containerCmd = dockerClient.createContainerCmd(DockerConstant.DOCKER_IMAGE_NAME);
        HostConfig hostConfig = new HostConfig();
        hostConfig.withMemory(config.getMemoryLimit() * 1024L);
        hostConfig.withMemorySwap(DockerConstant.MEMORY_SWAP);
        hostConfig.withCpuCount(DockerConstant.CPU_COUNT);
        //hostConfig.withSecurityOpts(Arrays.asList("seccomp=安全管理配置字符串"));

        hostConfig.setBinds(new Bind(userCodeParentPath, new Volume(DockerConstant.DOCKER_MOUNT_DIR))
        ,new Bind(DockerConstant.QUESTION_IS_DIR, new Volume(DockerConstant.DOCKER_MOUNT_IO_DIR)));

        CreateContainerResponse createContainerResponse = containerCmd
                .withHostConfig(hostConfig)
//                .withNetworkDisabled(false)
                .withAttachStdin(true)
                .withAttachStderr(true)
                .withAttachStdout(true)
                .withTty(true)
                .exec();
        log.info(String.valueOf(createContainerResponse));
        String containerId = createContainerResponse.getId();

        // 启动容器
        dockerClient.startContainerCmd(containerId).exec();
        // todo: pool

        // docker exec keen_blackwell java -cp /app Main 1 3
        // 执行命令并获取结果
        Long timeLimit = config.getTimeLimit();
        List<ExecuteMessage> executeMessageList = new ArrayList<>();

        for (String input : inputList) {
            ExecuteMessage executeMessage = new ExecuteMessage();

//            // 本地文件重定向方式
//            String[] command = ArrayUtil.clone(DockerConstant.JAVA_EXECUTE);
//            command[command.length-1] = command[command.length-1] + input;

            InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
            String[] command = DockerConstant.JAVA_EXECUTE;
            try {
                ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(containerId)
                        .withAttachStdin(true)
                        .withAttachStdout(true)
                        .withAttachStderr(true)
//                        .withTty(true)
                        .withCmd(command)
                        .exec();
                log.info("创建执行命令：" + execCreateCmdResponse);

                // 开始计时
                long startTime = System.nanoTime();

                FrameResultCallback callback = new FrameResultCallback();

                boolean finishedInTime = dockerClient.execStartCmd(execCreateCmdResponse.getId())
                        .withStdIn(inputStream)
                        .exec(callback)
                        .awaitCompletion(timeLimit, TimeUnit.SECONDS);

                // 打印捕获的输出内容
                executeMessage.setOutput(callback.getOutputLines());

                if (!finishedInTime) {
                    executeMessage.setJudge(JudgeResultEnum.TIME_LIMIT_EXCEEDED);
                }

                // 结束计时并计算执行时间
                long endTime = System.nanoTime();
                long executionTimeMillis = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
                executeMessage.setTime(executionTimeMillis);
                inputStream.close();
//                // 获取容器退出状态
//                Long exitCode = dockerClient.inspectContainerCmd(containerId).exec().getState().getExitCodeLong();
//                log.info("容器退出状态码: " + exitCode);

            } catch (InterruptedException | IOException e) {
                log.info("容器运行时，超时打断任务异常或输入异常");
                throw new RuntimeException(e);
            }
            executeMessageList.add(executeMessage);
        }

        // todo: pool
        // 停止容器
        dockerClient.stopContainerCmd(containerId).exec();
        // 删除容器
        dockerClient.removeContainerCmd(containerId).exec();

        try {
            dockerClient.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // todo: pool

        return executeMessageList;
    }

    static class FrameResultCallback extends ResultCallbackTemplate<FrameResultCallback, Frame> {
        private final List<String> outputLines = new ArrayList<>();

        @Override
        public void onNext(Frame frame) {
            if (frame.getStreamType() == StreamType.STDOUT || frame.getStreamType() == StreamType.STDERR) {
                String line = new String(frame.getPayload()).trim();
                outputLines.add(line);
                log.info((frame.getStreamType() == StreamType.STDOUT ? "STDOUT: " : "STDERR: ") + line);
            }
        }

        public String getOutputLines() {
            return Arrays.toString(outputLines.toArray(new String[0]));
        }
    }
}
