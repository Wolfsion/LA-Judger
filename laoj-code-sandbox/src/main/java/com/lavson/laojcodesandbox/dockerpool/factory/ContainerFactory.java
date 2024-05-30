package com.lavson.laojcodesandbox.dockerpool.factory;

import com.github.dockerjava.api.DockerClient;
import com.lavson.laojcodesandbox.dockerpool.task.DockerContainer;
import com.lavson.laojcodesandbox.dockerpool.task.Executable;
import com.lavson.model.entity.JudgeConfig;

/**
 * todo
 *
 * @author LA
 * @version 1.0
 * 2024/5/29 - 20:20
 */
public interface ContainerFactory {
    DockerContainer createDockerContainer(JudgeConfig config);

    void setClient(DockerClient dockerClient);
}
