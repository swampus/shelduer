package io.github.swampus.core.access;

import io.github.swampus.policy.AccessPolicy;
import io.github.swampus.spi.access.AccessLedger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * In-memory implementation of AccessLedger with support for FAIR locking.
 */
public class InMemoryAccessLedger implements AccessLedger {

    private final Map<String, ReentrantLock> lockMap = new ConcurrentHashMap<>();

    private final ReentrantLock lock = new ReentrantLock(/* fair = */ true);

    @Override
    public boolean tryAcquire(String key, AccessPolicy policy, long timeoutMs) {

        try {
            switch (policy) {
                case FAIR -> {
                    lock.lock(); // strict blocking with FIFO fairness
                    return true;
                }
                case SKIP_IF_BUSY -> {
                    return lock.tryLock(0, TimeUnit.MILLISECONDS);
                }
                default -> throw new UnsupportedOperationException("Policy not supported: " + policy);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public void release(String key) {
        ReentrantLock lock = lockMap.get(key);
        if (lock != null && lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

    @Override
    public void clearAll() {
        lockMap.clear();
    }

    private ReentrantLock createLock(AccessPolicy policy) {
        return new ReentrantLock(policy == AccessPolicy.FAIR);
    }
}

