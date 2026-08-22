package com.example.cqrsdemo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cqrsdemo.command.PlaceOrderRequest;
import com.example.cqrsdemo.handler.OrderCommandHandler;
import com.example.cqrsdemo.projection.OrderProjection;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderCommandHandler commandHandler;
    private final OrderProjection projection;

    public OrderController(OrderCommandHandler commandHandler, OrderProjection projection) {
        this.commandHandler = commandHandler;
        this.projection = projection;
    }

    @PostMapping
    public ResponseEntity<?> placeOrder(@RequestBody PlaceOrderRequest req) {
        commandHandler.handlePlaceOrder(req);
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@PathVariable("id") String id) {
        var view = projection.get(id);
        if (view == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(view);
    }
}
