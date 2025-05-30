package io.github.swampus;

import io.github.swampus.spi.access.AccessLedger;
import io.github.swampus.core.access.InMemoryAccessLedger;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestAccessLedgerConfig {
    @Bean
    public AccessLedger accessLedger() {
        return new InMemoryAccessLedger();
    }
}
