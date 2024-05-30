package com.lavson.laojcodesandbox.dockerpool.queue;

import com.lavson.laojcodesandbox.dockerpool.task.Executable;

/**
 * todo
 *
 * @author LA
 * @version 1.0
 * 2024/5/29 - 20:05
 */
public interface ExecutableQueue {
    void offer(Executable executable);

    Executable poll();

    int size();
}
