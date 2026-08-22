package com.example.cqrsdemo.projection;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.example.cqrsdemo.event.Event;
import com.example.cqrsdemo.event.OrderPlacedEvent;

@Component
public class OrderProjection {
    private final Map<String, OrderView> readModel = new ConcurrentHashMap<>();

    public void onEvent(Event e) {
        if (e instanceof OrderPlacedEvent) {
            OrderPlacedEvent op = (OrderPlacedEvent) e;
            readModel.put(op.getAggregateId(), new OrderView(op.getAggregateId(), op.getCustomerId(), op.getAmount()));
        }
    }

    public OrderView get(String orderId) {
        return readModel.get(orderId);
    }

    public static class OrderView {
        public final String orderId;
        public final String customerId;
        public final BigDecimal amount;

        public OrderView(String orderId, String customerId, BigDecimal amount) {
            this.orderId = orderId;
            this.customerId = customerId;
            this.amount = amount;
        }
    }
}
