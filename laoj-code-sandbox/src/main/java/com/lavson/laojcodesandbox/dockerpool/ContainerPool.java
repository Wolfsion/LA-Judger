package com.lavson.laojcodesandbox.dockerpool;

import com.lavson.laojcodesandbox.dockerpool.task.Executable;

/**
 * todo
 *
 * @author LA
 * @version 1.0
 * 2024/5/29 - 20:05
 */
public interface ContainerPool{
    void add();
    void remove();
    void borrow(Executable executable);
    void requite();

    void shutdown();
    int getInitSize();
    int getMaxSize();
    int getCoreSize();
    int getActiveCount();
    int getQueueSize();
    boolean isShutdown();
}
