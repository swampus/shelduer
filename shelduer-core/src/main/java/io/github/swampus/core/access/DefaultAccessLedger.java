package io.github.swampus.core.access;

import io.github.swampus.policy.AccessPolicy;
import io.github.swampus.spi.access.AccessLedger;

public class DefaultAccessLedger implements AccessLedger {
    private final AccessLedger fair = new FairAccessLedger();
    private final AccessLedger defaultLedger = new InMemoryAccessLedger();

    @Override
    public boolean tryAcquire(String key, AccessPolicy policy, long timeoutMs) {
        return getLedger(policy).tryAcquire(key, policy, timeoutMs);
    }

    @Override
    public void release(String key) {
        // Release in both (or track which ledger owns which key)
        fair.release(key);
        defaultLedger.release(key);
    }

    @Override
    public void clearAll() {
        fair.clearAll();
        defaultLedger.clearAll();
    }

    private AccessLedger getLedger(AccessPolicy policy) {
        return switch (policy) {
            case FAIR -> fair;
            default -> defaultLedger;
        };
    }
}
