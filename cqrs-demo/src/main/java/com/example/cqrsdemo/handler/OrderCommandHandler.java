package com.example.cqrsdemo.handler;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.cqrsdemo.aggregate.OrderAggregate;
import com.example.cqrsdemo.command.PlaceOrderRequest;
import com.example.cqrsdemo.event.Event;
import com.example.cqrsdemo.store.InMemoryEventStore;

@Service
public class OrderCommandHandler {
    private final InMemoryEventStore eventStore;

    public OrderCommandHandler(InMemoryEventStore eventStore) {
        this.eventStore = eventStore;
    }

    public void handlePlaceOrder(PlaceOrderRequest req) {
        OrderAggregate agg = new OrderAggregate();
        agg.placeOrder(req.orderId, req.customerId, req.amount);
        List<Event> events = agg.getUncommittedEvents();
        eventStore.append(req.orderId, events);
    }
}
