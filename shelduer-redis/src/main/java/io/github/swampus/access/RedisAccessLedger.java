package io.github.swampus.access;

import io.github.swampus.policy.AccessPolicy;
import io.github.swampus.spi.access.AccessLedger;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

public class RedisAccessLedger implements AccessLedger {

    private final RedissonClient redisson;

    public RedisAccessLedger(RedissonClient redisson) {
        this.redisson = redisson;
    }

    @Override
    public boolean tryAcquire(String key, AccessPolicy policy, long timeoutMs) {
        try {
            RLock lock = redisson.getFairLock(key);
            return lock.tryLock(timeoutMs, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public void release(String key) {
        RLock lock = redisson.getFairLock(key);
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

    @Override
    public void clearAll() {
        //not necessary for REDIS
    }
}
