package com.example.cqrsdemo.aggregate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.example.cqrsdemo.event.Event;
import com.example.cqrsdemo.event.OrderPlacedEvent;

public class OrderAggregate {
    private String orderId;
    private BigDecimal total = BigDecimal.ZERO;
    private final List<Event> uncommitted = new ArrayList<>();

    public void placeOrder(String orderId, String customerId, BigDecimal amount) {
        // basic validation
        if (orderId == null || orderId.isBlank()) throw new IllegalArgumentException("orderId required");
        if (customerId == null || customerId.isBlank()) throw new IllegalArgumentException("customerId required");
        if (amount == null || amount.doubleValue() <= 0) throw new IllegalArgumentException("amount must be > 0");
        OrderPlacedEvent e = new OrderPlacedEvent(orderId, customerId, amount);
        apply(e);
        uncommitted.add(e);
    }

    private void apply(OrderPlacedEvent e) {
        this.orderId = e.getAggregateId();
        this.total = e.getAmount();
    }

    public List<Event> getUncommittedEvents() { return List.copyOf(uncommitted); }
}
