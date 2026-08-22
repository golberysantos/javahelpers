package com.example.cqrsdemo.store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import com.example.cqrsdemo.event.Event;

public class InMemoryEventStore {
    private final Map<String, List<Event>> streams = new ConcurrentHashMap<>();
    private final List<Consumer<Event>> listeners = Collections.synchronizedList(new ArrayList<>());

    public void append(String streamId, List<Event> events) {
        streams.computeIfAbsent(streamId, k -> Collections.synchronizedList(new ArrayList<>())).addAll(events);
        // publish to listeners synchronously
        for (Event e : events) {
            for (Consumer<Event> l : listeners) {
                try { l.accept(e); } catch (Exception ex) { ex.printStackTrace(); }
            }
        }
    }

    public List<Event> load(String streamId) {
        return streams.getOrDefault(streamId, List.of());
    }

    public void registerListener(Consumer<Event> listener) {
        listeners.add(listener);
    }
}
