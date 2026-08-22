package com.example.cqrsdemo.event;

import java.math.BigDecimal;
import java.util.UUID;

public class OrderPlacedEvent implements Event {
    private final String eventId = UUID.randomUUID().toString();
    private final String aggregateId;
    private final String customerId;
    private final BigDecimal amount;
    private final long timestamp = System.currentTimeMillis();

    public OrderPlacedEvent(String aggregateId, String customerId, BigDecimal amount) {
        this.aggregateId = aggregateId;
        this.customerId = customerId;
        this.amount = amount;
    }

    @Override
    public String getEventId() { return eventId; }

    @Override
    public String getAggregateId() { return aggregateId; }

    public String getCustomerId() { return customerId; }

    public BigDecimal getAmount() { return amount; }

    @Override
    public long getTimestamp() { return timestamp; }

    @Override
    public String getType() { return "OrderPlaced"; }
}
