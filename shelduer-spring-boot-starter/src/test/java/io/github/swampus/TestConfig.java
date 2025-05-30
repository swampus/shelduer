package io.github.swampus;

import io.github.swampus.aspect.AccessControlAspect;
import io.github.swampus.core.access.FairAccessLedger;
import io.github.swampus.core.access.InMemoryAccessLedger;
import io.github.swampus.resolver.SpELKeyResolver;
import io.github.swampus.spi.access.AccessLedger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class TestConfig {

    @Bean
    public ExecutionLog executionLog() {
        return new ExecutionLog();
    }

    @Bean
    public OrderedService orderedService(ExecutionLog executionLog) {
        return new OrderedService(executionLog); // не создавай прокси сам
    }

    @Bean
    public AccessLedger accessLedger() {
        return new FairAccessLedger(); // <-- Должно быть FairAccessLedger
    }

    @Bean
    public SpELKeyResolver keyResolver() {
        return new SpELKeyResolver();
    }

    @Bean
    public AccessControlAspect accessControlAspect(AccessLedger ledger, SpELKeyResolver resolver) {
        return new AccessControlAspect(ledger, resolver);
    }
}
