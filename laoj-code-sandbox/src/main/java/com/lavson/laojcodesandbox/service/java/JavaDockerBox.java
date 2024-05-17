package com.lavson.laojcodesandbox.service.java;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.ArrayUtil;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import com.lavson.common.constant.DockerConstant;
import com.lavson.common.constant.SandBoxConstant;
import com.lavson.laojcodesandbox.dockerpool.DockerClientPool;
import com.lavson.model.codesandbox.ExecuteCodeRequest;
import com.lavson.model.codesandbox.ExecuteMessage;
import com.lavson.model.entity.JudgeConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

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

        // todo: pool
        // 容器池获取客户端
        DockerClient dockerClient = DockerClientPool.dockerClient();

        // 创建容器
        CreateContainerCmd containerCmd = dockerClient.createContainerCmd(DockerConstant.DOCKER_IMAGE_NAME);
        HostConfig hostConfig = new HostConfig();
        hostConfig.withMemory(config.getMemoryLimit() * 1024L);
        hostConfig.withMemorySwap(DockerConstant.MEMORY_SWAP);
        hostConfig.withCpuCount(DockerConstant.CPU_COUNT);
        //hostConfig.withSecurityOpts(Arrays.asList("seccomp=安全管理配置字符串"));
        hostConfig.setBinds(new Bind(userCodeParentPath, new Volume(DockerConstant.DOCKER_MOUNT_DIR)));
        CreateContainerResponse createContainerResponse = containerCmd
                .withHostConfig(hostConfig)
                .withNetworkDisabled(true)
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
            final long[] maxMemory = {0L};

            ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(containerId)
                    .withCmd(DockerConstant.JAVA_EXECUTE)
                    .withAttachStderr(true)
                    .withAttachStdin(true)
                    .withAttachStdout(true)
                    .exec();
            log.info("创建执行命令：" + execCreateCmdResponse);
            String execId = execCreateCmdResponse.getId();

            StatsCmd statsCmd = openStat(dockerClient, containerId, maxMemory);

            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))) {
                // 开始执行命令并传递输入数据
                dockerClient.execStartCmd(execId)
                        .withDetach(false)
                        .withTty(false)
                        .withStdIn(inputStream)
                        .exec(new ResultCallback<Frame>() {
                            @Override
                            public void close() {
                                log.info("close()");
                            }

                            @Override
                            public void onStart(Closeable closeable) {
                                log.info("start()");
                            }

                            @Override
                            public void onNext(Frame object) {
                                // 处理输出结果
                                String output = new String(object.getPayload(), StandardCharsets.UTF_8);
                                executeMessage.setOutput(output);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                log.info("error()");
                            }

                            @Override
                            public void onComplete() {
                                log.info("complete()");
                            }
                        });

            } catch (IOException e) {
                log.info(String.valueOf(e));
                throw new RuntimeException(e);
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            executeMessage.setMemory(maxMemory[0] / 1024L);
            executeMessageList.add(executeMessage);

            statsCmd.close();
        }

        log.info("Test");

        // todo: pool
        // 停止容器
        dockerClient.stopContainerCmd(containerId).exec();
        // 删除容器
        dockerClient.removeContainerCmd(containerId).exec();
        // todo: pool

        return executeMessageList;
    }

    public static StatsCmd openStat(DockerClient dockerClient, String containerId, long[] maxMemory) {
        // 获取占用的内存
        StatsCmd statsCmd = dockerClient.statsCmd(containerId);
        ResultCallback<Statistics> resultCallback = new ResultCallback<Statistics>() {

            @Override
            public void close() throws IOException {

            }

            @Override
            public void onStart(Closeable closeable) {

            }

            @Override
            public void onNext(Statistics statistics) {
                System.out.println("内存占用：" + statistics.getMemoryStats().getUsage());
                maxMemory[0] = Math.max(statistics.getMemoryStats().getUsage(), maxMemory[0]);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }


        };

        ResultCallback<Statistics> statisticsResultCallback = statsCmd.exec(resultCallback);
        statsCmd.exec(statisticsResultCallback);
        return statsCmd;
    }

}
