package io.github.swampus.config;

import io.github.swampus.spi.access.AccessLedger;
import io.github.swampus.core.access.DefaultAccessLedger;
import io.github.swampus.resolver.SpELKeyResolver;
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
}
