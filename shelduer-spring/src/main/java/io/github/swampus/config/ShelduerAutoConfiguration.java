package io.github.swampus.config;

import io.github.swampus.ShelduerClient;
import io.github.swampus.access.IAccessManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShelduerAutoConfiguration {
    @Bean
    public IAccessManager shelduerAccessManager() {
        return new ShelduerClient();
    }

}
