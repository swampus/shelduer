package io.github.swampus.redis;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.GenericContainer;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class RedisAccessLedgerTest {

    @Container
    private static final GenericContainer<?> redis =
            new GenericContainer<>("redis:7.2.4-alpine").withExposedPorts(6379);

    @Test
    void testRedisConnection() {
        String redisAddress = "redis://" + redis.getHost() + ":" + redis.getFirstMappedPort();

        Config config = new Config();
        config.useSingleServer().setAddress(redisAddress);

        RedissonClient client = Redisson.create(config);
        assertNotNull(client);

        client.getBucket("test-key").set("Hello Redis!");
        assertEquals("Hello Redis!", client.getBucket("test-key").get());

        client.shutdown();
    }
}
