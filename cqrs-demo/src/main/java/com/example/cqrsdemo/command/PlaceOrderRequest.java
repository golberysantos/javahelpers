package com.example.cqrsdemo.command;

import java.math.BigDecimal;

public class PlaceOrderRequest {
    public String orderId;
    public String customerId;
    public BigDecimal amount;

    public PlaceOrderRequest() {}

    public PlaceOrderRequest(String orderId, String customerId, BigDecimal amount) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.amount = amount;
    }
}
