package com.lavson.laojcodesandbox.dockerpool.queue;

import com.lavson.laojcodesandbox.dockerpool.ContainerPool;
import com.lavson.laojcodesandbox.dockerpool.DenyPolicy;
import com.lavson.laojcodesandbox.dockerpool.DockerContainerPool;
import com.lavson.laojcodesandbox.dockerpool.task.Executable;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.locks.LockSupport;

/**
 * todo
 *
 * @author LA
 * @version 1.0
 * 2024/5/29 - 20:35
 */
@Slf4j
public class LinkedExecutableQueue implements ExecutableQueue {
    private final int limit;
    private final DenyPolicy denyPolicy;
    private final ConcurrentLinkedDeque<Executable> executableLinkedList;
    private final ContainerPool containerPool;

    private static Integer lock = 1;

    public LinkedExecutableQueue(int limit, DenyPolicy denyPolicy, ContainerPool containerPool) {
        this.limit = limit;
        this.denyPolicy = denyPolicy;
        this.executableLinkedList = new ConcurrentLinkedDeque<>();
        this.containerPool = containerPool;
    }

    @Override
    public void offer(Executable executable) {
        synchronized (executableLinkedList) {
            if (executableLinkedList.size() >= limit) {
                denyPolicy.reject(executable, containerPool);
            } else {
                executableLinkedList.addLast(executable);
                executableLinkedList.notify();
            }
        }
    }

    @Override
    public Executable poll() {
        // todo: debug 未锁住其他线程
        synchronized (executableLinkedList) {
            while (executableLinkedList.isEmpty()) {
                try {
                    executableLinkedList.wait();
                } catch (InterruptedException e) {
                    log.error("执行任务队列阻塞异常.");
                    e.printStackTrace();
                }
            }
        }
        return executableLinkedList.removeFirst();
    }

    @Override
    public int size() {
        return executableLinkedList.size();
    }
}
