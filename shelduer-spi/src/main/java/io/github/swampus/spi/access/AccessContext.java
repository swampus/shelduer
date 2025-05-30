package io.github.swampus.spi.access;

import io.github.swampus.policy.AccessPolicy;
import java.time.Duration;
import java.util.Optional;
public interface AccessContext {

    Object key();

    AccessPolicy policy();

    Optional<Duration> timeout();

    Optional<String> loginTag();

    Optional<Integer> priority();
}
