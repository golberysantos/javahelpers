package com.example.cqrsdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.cqrsdemo.store.InMemoryEventStore;
import com.example.cqrsdemo.projection.OrderProjection;

@SpringBootApplication
public class CqrsDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CqrsDemoApplication.class, args);
    }

    @Bean
    public InMemoryEventStore eventStore(OrderProjection projection) {
        InMemoryEventStore store = new InMemoryEventStore();
        store.registerListener(projection::onEvent);
        return store;
    }
}
