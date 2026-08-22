package com.example.cqrsdemo.event;

public interface Event {
    String getEventId();
    String getAggregateId();
    long getTimestamp();
    String getType();
}
