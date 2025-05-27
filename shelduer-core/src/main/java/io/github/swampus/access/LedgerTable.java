package io.github.swampus.access;

import io.github.swampus.policy.AccessPolicy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A simple ledger that tracks access to logical resources by key.
 */
public class LedgerTable {

    private final Map<String, ReentrantLock> lockMap = new ConcurrentHashMap<>();

    public boolean tryAcquire(String key, AccessPolicy policy, long timeoutMs) {
        ReentrantLock lock = lockMap.computeIfAbsent(key, k -> new ReentrantLock());

        try {
            switch (policy) {
                case EXCLUSIVE -> {
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

    public void release(String key) {
        ReentrantLock lock = lockMap.get(key);
        if (lock != null && lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

    public void clearAll() {
        lockMap.clear();
    }
}
