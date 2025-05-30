package io.github.swampus.core.access;

import io.github.swampus.policy.AccessPolicy;
import io.github.swampus.spi.access.AccessLedger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FairAccessLedger implements AccessLedger {

    private final Map<String, Lock> lockMap = new ConcurrentHashMap<>();

    @Override
    public boolean tryAcquire(String key, AccessPolicy policy, long timeoutMs) {
        Lock lock = lockMap.computeIfAbsent(key, k -> new ReentrantLock(true)); // fair lock
        try {
            return lock.tryLock(timeoutMs, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public void release(String key) {
        Lock lock = lockMap.get(key);
        if (lock != null) {
            lock.unlock();
        }
    }

    @Override
    public void clearAll() {
        lockMap.clear();
    }
}
