package io.github.swampus;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = TestConfig.class)
public class FairAccessPolicyTest {

    @Autowired
    OrderedService service;

    @Autowired
    ExecutionLog log;

    @Test
    public void testFairAccessOrder() throws InterruptedException {
        log.clear();
        int totalClients = 5;
        ExecutorService executor = Executors.newFixedThreadPool(totalClients);

        CountDownLatch readyLatch = new CountDownLatch(totalClients);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch[] orderLatches = new CountDownLatch[totalClients];
        for (int i = 0; i < totalClients; i++) {
            orderLatches[i] = new CountDownLatch(i == 0 ? 0 : 1);
        }

        for (int i = 0; i < totalClients; i++) {
            final int clientId = i;
            executor.submit(() -> {
                try {
                    readyLatch.countDown();
                    startLatch.await();

                    orderLatches[clientId].await();
                    service.doWork(clientId);

                    if (clientId + 1 < totalClients) {
                        orderLatches[clientId + 1].countDown();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            });
        }

        readyLatch.await();
        startLatch.countDown();

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        List<Integer> actualOrder = log.getOrder();
        System.out.println("Execution Order: " + actualOrder);

        assertEquals(List.of(0, 1, 2, 3, 4), actualOrder);
    }
}
