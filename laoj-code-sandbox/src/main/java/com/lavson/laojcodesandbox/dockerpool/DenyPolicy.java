package com.lavson.laojcodesandbox.dockerpool;

import com.lavson.common.exception.ExecuteDenyException;
import com.lavson.laojcodesandbox.dockerpool.task.Executable;

/**
 * todo
 *
 * @author LA
 * @version 1.0
 * 2024/5/29 - 20:10
 */
@FunctionalInterface
public interface DenyPolicy {
    void reject(Executable executable, ContainerPool containerPool);

    class DiscardDenyPolicy implements DenyPolicy {
        @Override
        public void reject(Executable executable, ContainerPool containerPool) {

        }
    }

    class AbortDenyPolicy implements DenyPolicy {
        @Override
        public void reject(Executable executable, ContainerPool containerPool) {
            throw new ExecuteDenyException(executable + "will be discarded.");
        }
    }

    // todo: Future consideration to implement
    class MessageQueuePolicy implements DenyPolicy {
        @Override
        public void reject(Executable executable, ContainerPool containerPool) {

        }
    }

}
