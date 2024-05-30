package com.lavson.laojcodesandbox.service.java;

import cn.hutool.core.util.StrUtil;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallbackTemplate;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import com.lavson.common.constant.DockerConstant;
import com.lavson.common.constant.SandBoxConstant;
import com.lavson.laojcodesandbox.config.DockerConfig;
import com.lavson.laojcodesandbox.dockerpool.DockerContainerPool;
import com.lavson.laojcodesandbox.dockerpool.task.Executable;
import com.lavson.model.codesandbox.ExecuteMessage;
import com.lavson.model.entity.JudgeConfig;
import com.lavson.model.enums.JudgeResultEnum;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.*;
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
public class JavaDockerBox extends JavaCodeBoxTemplate{

    static final Pattern pattern = Pattern.compile(DockerConstant.TIME_MEMORY_REGEX);

    private final DockerClient dockerClient;
    private final DockerContainerPool dockerContainerPool;

    private boolean waitingLock = true;

    /**
     * 3、创建容器，把文件复制到容器内
     * @param userCodeFile
     * @param inputList
     * @return
     */
    @Override
    public List<ExecuteMessage> runFile(File userCodeFile, List<String> inputList, JudgeConfig config) {
        String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();
        String last = new File(userCodeParentPath).getName();

        Long timeLimit = config.getTimeLimit();
        List<ExecuteMessage> executeMessageList = new ArrayList<>();

        // docker exec keen_blackwell java -cp /app Main 1 3
        // 执行命令并获取结果
        waitingLock = true;
        dockerContainerPool.borrow(containerId -> {
            String[] command = deepCopyStringArray(DockerConstant.TIME_MEMORY_JAVA_EXECUTE);
            // todo: only in linux
            command[command.length-1] = command[command.length-1] + "/" +
                    last + SandBoxConstant.GLOBAL_JAVA_MAIN;
            for (String input : inputList) {
                ExecuteMessage executeMessage = new ExecuteMessage();
                InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
                try {
                    ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(containerId)
                            .withAttachStdin(true)
                            .withAttachStdout(true)
                            .withAttachStderr(true)
                            .withCmd(command)
                            .exec();
                    log.info("创建执行命令：" + execCreateCmdResponse);

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
                    inputStream.close();
                    Long[] timeAndMemory = parseTimeAndMemory(callback.getTimeAndMemory());
                    executeMessage.setTime(timeAndMemory[DockerConstant.INDEX_TIME]);
                    executeMessage.setMemory(timeAndMemory[DockerConstant.INDEX_MEMORY]);

                } catch (InterruptedException | IOException e) {
                    log.info("容器运行时，超时打断任务异常或输入异常");
                    throw new RuntimeException(e);
                }
                executeMessageList.add(executeMessage);
            }
            waitingLock = false;
        });

        // todo: optim 自旋锁换阻塞唤醒锁
        while (waitingLock) {

        }
        return executeMessageList;
    }
    static String[] deepCopyStringArray(String[] array) {
        if (array == null) {
            return null;
        }

        String[] newArray = new String[array.length];
        System.arraycopy(array, 0, newArray, 0, array.length);

        return newArray;
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
