package com.lavson.laojcodesandbox.service.java;

import cn.hutool.core.util.StrUtil;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallbackTemplate;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.model.*;
import com.lavson.common.constant.DockerConstant;
import com.lavson.laojcodesandbox.dockerpool.DockerContainerPool;
import com.lavson.model.codesandbox.ExecuteMessage;
import com.lavson.model.entity.JudgeConfig;
import com.lavson.model.enums.JudgeResultEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * todo
 *
 * @author LA
 * @version 1.0
 * 2024/5/14 - 20:31
 */


@Slf4j
@Component
@RequiredArgsConstructor
@Deprecated
public class JavaDockerBoxOld extends JavaCodeBoxTemplate{

    static final Pattern pattern = Pattern.compile(DockerConstant.TIME_MEMORY_REGEX);

    private final DockerClient dockerClient;
    private final DockerContainerPool dockerContainerPool;

    /**
     * 3、创建容器，把文件复制到容器内
     * @param userCodeFile
     * @param inputList
     * @return
     */
    @Override
    public List<ExecuteMessage> runFile(File userCodeFile, List<String> inputList, JudgeConfig config) {
        String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();
        log.info("Docker Pool:"+dockerClient.hashCode());
        // todo: pool, parameter: memory_limit

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
            String[] command = DockerConstant.TIME_MEMORY_JAVA_EXECUTE;
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
                        .awaitCompletion(timeLimit, TimeUnit.MILLISECONDS);

                // 打印捕获的输出内容
                executeMessage.setOutput(callback.getOutputLines());
                executeMessage.setErrorMessage(callback.getErrorMessages());

                if (!finishedInTime) {
                    executeMessage.setJudge(JudgeResultEnum.TIME_LIMIT_EXCEEDED);
                }

                // 结束计时并计算执行时间
                long endTime = System.nanoTime();
                long executionTimeMillis = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
                executeMessage.setTime(executionTimeMillis);
                inputStream.close();

                Long[] timeAndMemory = parseTimeAndMemory(callback.getTimeAndMemory());
                executeMessage.setTime(timeAndMemory[DockerConstant.INDEX_TIME]);
                executeMessage.setMemory(timeAndMemory[DockerConstant.INDEX_MEMORY]);

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

        // todo: pool

        return executeMessageList;
    }

    static Long[] parseTimeAndMemory(String line) {
        if (StrUtil.isBlank(line)) {
            log.error("An invalid String that must not be empty");
            return new Long[]{0L, 0L};
        }
        Matcher matcher = pattern.matcher(line);
        Long[] rets = new Long[DockerConstant.METRIC_LEN];
        if (matcher.find()) {
            rets[DockerConstant.INDEX_TIME] = (long)(Double.parseDouble(matcher.group(1)) * 1000L);
            rets[DockerConstant.INDEX_MEMORY] = Long.parseLong(matcher.group(2));
        } else {
            rets[DockerConstant.INDEX_TIME] = 0L;
            rets[DockerConstant.INDEX_MEMORY] = 0L;
        }
        return rets;
    }

    static class FrameResultCallback extends ResultCallbackTemplate<FrameResultCallback, Frame> {
        private final List<String> outputLines = new ArrayList<>();
        private final List<String> errorMessages = new ArrayList<>();

        @Getter
        private String timeAndMemory = "";

        @Override
        public void onNext(Frame frame) {
            StreamType type = frame.getStreamType();
            if (type == StreamType.STDOUT || type == StreamType.STDERR) {
                String line = new String(frame.getPayload()).trim();

                if (line.contains(DockerConstant.TIME_MEMORY_MARK)) {
                    timeAndMemory = line;
                    log.info("Time and Memory Cost: " + line);
                } else {
                    if (type == StreamType.STDOUT) {
                        outputLines.add(line);
                    } else {
                        errorMessages.add(line);
                    }
                    log.info((type == StreamType.STDOUT ? "STDOUT: " : "STDERR: ") + line);
                }
            }
        }

        public String getOutputLines() {
            return Arrays.toString(outputLines.toArray(new String[0]));
        }
        public String getErrorMessages() {
            return Arrays.toString(errorMessages.toArray(new String[0]));
        }
    }
}
