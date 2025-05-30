package io.github.swampus.config;

import io.github.swampus.access.RedisAccessLedger;
import io.github.swampus.core.access.DefaultAccessLedger;
import io.github.swampus.core.access.FairAccessLedger;
import io.github.swampus.resolver.SpELKeyResolver;
import io.github.swampus.spi.access.AccessLedger;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShelduerAutoConfiguration {

    @Bean
    public AccessLedger accessLedger() {
        return new DefaultAccessLedger();
    }

    @Bean
    public SpELKeyResolver spELKeyResolver() {
        return new SpELKeyResolver();
    }

    @Bean
    @ConditionalOnProperty(name = "shelduer.ledger.type", havingValue = "redis")
    public AccessLedger redisAccessLedger(RedissonClient redisson) {
        return new RedisAccessLedger(redisson);
    }

    @Bean
    @ConditionalOnProperty(name = "shelduer.ledger.type", havingValue = "memory",
            matchIfMissing = true)
    public AccessLedger inMemoryLedger() {
        return new FairAccessLedger();
    }
}
