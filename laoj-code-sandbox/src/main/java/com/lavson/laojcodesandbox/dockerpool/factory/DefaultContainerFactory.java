package com.lavson.laojcodesandbox.dockerpool.factory;

import com.github.dockerjava.api.DockerClient;
import com.lavson.laojcodesandbox.dockerpool.queue.ExecutableQueue;
import com.lavson.laojcodesandbox.dockerpool.task.DockerContainer;
import com.lavson.laojcodesandbox.dockerpool.task.Executable;
import com.lavson.model.entity.JudgeConfig;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
@AllArgsConstructor
public class DefaultContainerFactory implements ContainerFactory {
    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    public DockerClient dockerClient;

    @Override
    public DockerContainer createDockerContainer(JudgeConfig config) {
        COUNTER.getAndDecrement();
        return new DockerContainer(config, dockerClient);
    }

    @Override
    public void setClient(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

}