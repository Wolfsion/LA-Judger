package com.lavson.laojcodesandbox.dockerpool.task;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Volume;
import com.lavson.common.constant.DockerConstant;
import com.lavson.laojcodesandbox.dockerpool.task.Executable;
import com.lavson.model.entity.JudgeConfig;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * todo
 *
 * @author LA
 * @version 1.0
 * 2024/5/29 - 20:25
 */
@Slf4j
@Data
@NoArgsConstructor
public class DockerContainer {
    @Getter
    private String id;
    @Setter
    private Executable executable;
    private DockerClient dockerClient;
    private JudgeConfig config;
    private String codePath = DockerConstant.CODE_DIR;

    public DockerContainer(JudgeConfig config, DockerClient dockerClient) {
        this.config = config;
        this.dockerClient = dockerClient;
    }

    public DockerContainer(JudgeConfig config, DockerClient dockerClient, Executable executable) {
        this.config = config;
        this.dockerClient = dockerClient;
        this.executable = executable;
    }

    public void start() {
        // 创建容器
        CreateContainerCmd containerCmd = dockerClient.createContainerCmd(DockerConstant.DOCKER_IMAGE_NAME);
        HostConfig hostConfig = new HostConfig();
        hostConfig.withMemory(config.getMemoryLimit() * 1024L);
        hostConfig.withMemorySwap(DockerConstant.MEMORY_SWAP);
        hostConfig.withCpuCount(DockerConstant.CPU_COUNT);

        hostConfig.setBinds(new Bind(codePath, new Volume(DockerConstant.DOCKER_MOUNT_DIR)));

        CreateContainerResponse createContainerResponse = containerCmd
                .withHostConfig(hostConfig)
                .withAttachStdin(true)
                .withAttachStderr(true)
                .withAttachStdout(true)
                .withTty(true)
                .exec();
        log.info(String.valueOf(createContainerResponse));
        String containerId = createContainerResponse.getId();

        // 启动容器
        dockerClient.startContainerCmd(containerId).exec();
        this.id = containerId;
    }

    public void stop() {
        dockerClient.stopContainerCmd(id).exec();
        // 删除容器
        dockerClient.removeContainerCmd(id).exec();
    }

}
