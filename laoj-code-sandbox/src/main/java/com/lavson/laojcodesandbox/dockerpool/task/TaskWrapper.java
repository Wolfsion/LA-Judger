package com.lavson.laojcodesandbox.dockerpool.task;

import com.lavson.laojcodesandbox.dockerpool.queue.ExecutableQueue;
import lombok.extern.slf4j.Slf4j;

/**
 * todo
 *
 * @author LA
 * @version 1.0
 * 2024/5/29 - 20:44
 */
@Slf4j
public class TaskWrapper extends Thread {
    private final ExecutableQueue executableQueue;
    private final DockerContainer dockerContainer;
    private volatile boolean running = true;

    public TaskWrapper(ExecutableQueue executableQueue, DockerContainer dockerContainer) {
        this.executableQueue = executableQueue;
        this.dockerContainer = dockerContainer;
    }

    public void stopContainer() {
        this.running = false;
        this.dockerContainer.stop();
    }

    @Override
    public void run() {
        this.dockerContainer.start();
        while (running && !Thread.currentThread().isInterrupted()) {
            try {
                Executable task = executableQueue.poll();
                task.executeCmd(dockerContainer.getId());
            } catch (Exception e) {
                //running = false;
                log.error("TaskWrapper任务执行异常");
                e.printStackTrace();
            }
        }
    }
}
