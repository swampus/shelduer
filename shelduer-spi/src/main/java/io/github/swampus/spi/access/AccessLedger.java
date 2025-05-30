package io.github.swampus.spi.access;

import io.github.swampus.policy.AccessPolicy;

public interface AccessLedger {
    boolean tryAcquire(String key, AccessPolicy policy, long timeoutMs);
    void release(String key);
    void clearAll();
}
