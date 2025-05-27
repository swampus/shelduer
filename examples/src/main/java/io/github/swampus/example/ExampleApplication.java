package io.github.swampus.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import io.github.swampus.access.AccessControlled;

@SpringBootApplication
public class ExampleApplication implements CommandLineRunner {

    private final AIModule aiModule;

    public ExampleApplication(AIModule aiModule) {
        this.aiModule = aiModule;
    }

    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("🚀 Starting synthetic AI demo...");

        for (int i = 0; i < 5; i++) {
            int id = i;
            new Thread(() -> aiModule.retrain(id)).start();
        }
    }

    @Component
    public static class AIModule {

        @AccessControlled(value = "ai.retrain")
        public void retrain(int id) {
            System.out.println("🧠 [Thread " + id + "] trying to retrain...");
            try {
                Thread.sleep(2000);
                System.out.println("✅ [Thread " + id + "] retraining complete.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
