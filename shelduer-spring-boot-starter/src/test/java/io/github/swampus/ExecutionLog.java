package io.github.swampus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExecutionLog {
    private final List<Integer> order = Collections.synchronizedList(new ArrayList<>());

    public void log(int id) {
        System.out.println("[LOG] Add " + id);
        order.add(id);
    }

    public List<Integer> getOrder() {
        return new ArrayList<>(order);
    }

    public void clear() {
        order.clear();
    }
}