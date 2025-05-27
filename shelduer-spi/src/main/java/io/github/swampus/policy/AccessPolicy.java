package io.github.swampus.policy;

public enum AccessPolicy {
    EXCLUSIVE,
    FAIR,
    LAST_WRITE_WINS,
    SKIP_IF_BUSY
}
