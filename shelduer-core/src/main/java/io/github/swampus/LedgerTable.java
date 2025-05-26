package io.github.swampus;


import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory access control table.
 */
@Slf4j
public class LedgerTable {
    private final Map<String, Boolean> accessMap = new ConcurrentHashMap<>();

    public void allow(String key) {
        accessMap.put(key, true);
        log.debug("Access allowed: {}", key);
    }

    public void block(String key) {
        accessMap.put(key, false);
        log.debug("Access blocked: {}", key);
    }

    public boolean isAllowed(String key) {
        return accessMap.getOrDefault(key, false);
    }

    public void revokeAll() {
        accessMap.clear();
        log.debug("All access rules revoked.");
    }
}
