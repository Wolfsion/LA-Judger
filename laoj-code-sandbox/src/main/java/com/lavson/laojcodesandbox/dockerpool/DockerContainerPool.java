package com.lavson.laojcodesandbox.dockerpool;

import com.github.dockerjava.api.DockerClient;
import com.lavson.common.constant.SandBoxConstant;
import com.lavson.laojcodesandbox.dockerpool.factory.ContainerFactory;
import com.lavson.laojcodesandbox.dockerpool.factory.DefaultContainerFactory;
import com.lavson.laojcodesandbox.dockerpool.queue.ExecutableQueue;
import com.lavson.laojcodesandbox.dockerpool.queue.LinkedExecutableQueue;
import com.lavson.laojcodesandbox.dockerpool.task.DockerContainer;
import com.lavson.laojcodesandbox.dockerpool.task.Executable;
import com.lavson.laojcodesandbox.dockerpool.task.TaskWrapper;
import com.lavson.model.entity.JudgeConfig;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.InputStream;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * todo
 *
 * @author LA
 * @version 1.0
 * 2024/5/16 - 19:48
 */
@Slf4j
@Component
public class DockerContainerPool extends Thread implements ContainerPool {
    private final DockerClient dockerClient;
    private final int initSize;
    private final int maxSize;
    private final int coreSize;
    private final int queueSize;
    private int activeCount;
    private final ContainerFactory containerFactory;
    private final ExecutableQueue executableQueue;

    private volatile boolean isShutdown = false;

    private final static DenyPolicy DEFAULT_DENY_POLICY = new DenyPolicy.DiscardDenyPolicy();
    private final static ContainerFactory DEFAULT_CONTAINER_FACTORY =
            new DefaultContainerFactory(null);
    private final long keepAliveTime;
    private final TimeUnit timeUnit;
    private final ConcurrentLinkedDeque<TaskWrapper> tasks = new ConcurrentLinkedDeque<>();

    @Setter
    private JudgeConfig config = new JudgeConfig(SandBoxConstant.DEFAULT_TIME_LIMIT,
            SandBoxConstant.DEFAULT_MEMORY_LIMIT, 0L);
    private boolean initialized = false;

    @Autowired
    public DockerContainerPool(DockerClient dockerClient,
                               @Value("${docker.pool.initSize:5}") int initSize,
                               @Value("${docker.pool.maxSize:20}") int maxSize,
                               @Value("${docker.pool.coreSize:10}") int coreSize,
                               @Value("${docker.pool.queueSize:10}") int queueSize) {
        this(dockerClient,
                initSize, maxSize, coreSize, queueSize,
                DEFAULT_DENY_POLICY, DEFAULT_CONTAINER_FACTORY,
                2, TimeUnit.SECONDS);
    }

    public DockerContainerPool(DockerClient dockerClient,
                               int initSize, int maxSize, int coreSize, int queueSize,
                               DenyPolicy denyPolicy, ContainerFactory containerFactory,
                               long keepAliveTime, TimeUnit timeUnit) {
        this.dockerClient = dockerClient;
        this.initSize = initSize;
        this.maxSize = maxSize;
        this.coreSize = coreSize;
        this.queueSize = queueSize;
        this.containerFactory = containerFactory;
        this.executableQueue = new LinkedExecutableQueue(queueSize, denyPolicy, this);
        this.keepAliveTime = keepAliveTime;
        this.timeUnit = timeUnit;
        this.init();
    }

    @PostConstruct
    private void init() {
        if (!initialized) {
            this.containerFactory.setClient(dockerClient);
            IntStream.range(0, initSize).forEach(i -> add());
            initialized = true;
            log.info("容器池初始化成功.");
//            this.start();
        }
    }

    @PreDestroy
    public void exit() {
        // todo: debug 未调用
        log.info("容器池开始关闭.");
        IntStream.range(0, activeCount).forEach(i -> {
            remove();
            log.info("已关闭了1个，还有" + activeCount + "个");
        });
        log.info("容器已全部关闭.");
    }

    @Override
    public void add() {
        DockerContainer dockerContainer = this.containerFactory.createDockerContainer(config);
        TaskWrapper task = new TaskWrapper(executableQueue, dockerContainer);
        tasks.offer(task);
        this.activeCount++;
        task.start();
    }

    @Override
    public void remove() {
        TaskWrapper task = tasks.remove();
        task.stopContainer();
        this.activeCount--;
    }

    @Override
    public void borrow(Executable executable) {
        if (this.isShutdown) {
            throw new IllegalStateException("容器池已被销毁");
        }
        this.executableQueue.offer(executable);
    }

    @Override
    public void requite() {

    }

    @Override
    public void shutdown() {
        this.isShutdown = false;

    }

    @Override
    public int getInitSize() {
        return initSize;
    }

    @Override
    public int getMaxSize() {
        return maxSize;
    }

    @Override
    public int getCoreSize() {
        return coreSize;
    }

    @Override
    public int getActiveCount() {
        return activeCount;
    }

    @Override
    public int getQueueSize() {
        return queueSize;
    }

    @Override
    public boolean isShutdown() {
        return this.isShutdown;
    }

    @Override
    public void run() {
        while (!isShutdown && !isInterrupted()) {
            try {
                timeUnit.sleep(keepAliveTime);
            } catch (InterruptedException e) {
                log.error("容器池打断异常.");
                e.printStackTrace();
                isShutdown = true;
                break;
            }
            synchronized (this) {
                if (isShutdown) {
                    break;
                }

                if (executableQueue.size() > 0 && activeCount < coreSize) {
                    IntStream.range(initSize, coreSize).forEach(i -> add());
                    continue;
                }
                if (executableQueue.size() > 0 && activeCount < maxSize) {
                    IntStream.range(coreSize, maxSize).forEach(i -> add());
                }
                if (executableQueue.size() == 0 && activeCount > coreSize) {
                    IntStream.range(coreSize, activeCount).forEach(i -> remove());
                }
            }
        }
    }
}
