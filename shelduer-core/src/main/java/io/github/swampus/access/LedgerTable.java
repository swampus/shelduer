package io.github.swampus.access;

import io.github.swampus.policy.AccessPolicy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A simple ledger that tracks access to logical resources by key.
 */
public class LedgerTable {

    private final Map<String, ReentrantLock> lockMap = new ConcurrentHashMap<>();

    public boolean tryAcquire(String key, AccessPolicy policy, long timeoutMs) {
        if (policy != AccessPolicy.EXCLUSIVE) {
            throw new UnsupportedOperationException("Only EXCLUSIVE policy is supported.");
        }

        ReentrantLock lock = lockMap.computeIfAbsent(key, k -> new ReentrantLock());

        try {
            if (timeoutMs > 0) {
                return lock.tryLock(timeoutMs, java.util.concurrent.TimeUnit.MILLISECONDS);
            } else {
                return lock.tryLock();
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
