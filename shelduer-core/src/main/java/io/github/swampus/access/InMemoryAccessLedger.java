package io.github.swampus.access;

import io.github.swampus.policy.AccessPolicy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * In-memory implementation of AccessLedger with support for FAIR locking.
 */
public class InMemoryAccessLedger implements AccessLedger {

    private final Map<String, ReentrantLock> lockMap = new ConcurrentHashMap<>();

    @Override
    public boolean tryAcquire(String key, AccessPolicy policy, long timeoutMs) {
        ReentrantLock lock = lockMap.computeIfAbsent(key, k -> createLock(policy));

        try {
            switch (policy) {
                case EXCLUSIVE, FAIR -> {
                    if (timeoutMs > 0) {
                        return lock.tryLock(timeoutMs, TimeUnit.MILLISECONDS);
                    } else {
                        return lock.tryLock();
                    }
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
        boolean fair = policy == AccessPolicy.FAIR;
        return new ReentrantLock(fair);
    }
}

