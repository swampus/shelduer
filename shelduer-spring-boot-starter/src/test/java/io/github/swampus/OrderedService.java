package io.github.swampus;

import io.github.swampus.access.AccessControlled;
import io.github.swampus.policy.AccessPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


public class OrderedService {

    private ExecutionLog log;
    public OrderedService(ExecutionLog log) {
        this.log = log;
    }

    @AccessControlled(key = "'global'", policy = AccessPolicy.FAIR, timeoutMs = 5000)
    public void doWork(int id) {
        System.out.println("OrderedService.doWork(" + id + ")");
        log.log(id);
        try {
            Thread.sleep(100);
        } catch (InterruptedException ignored) {
        }
    }

    public List<Integer> getExecutionOrder() {
        return log.getOrder();
    }
}

