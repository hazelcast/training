package com.hazelcast;

import com.hazelcast.core.MapStore;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DummyMapStore implements MapStore<Integer, String> {
    private final DummyDatabase database = new DummyDatabase();

    public void store(Integer key, String value) {
        System.out.println("Informational: store called");
        database.store(key, value);
    }

    public void storeAll(Map<Integer, String> entries) {
        System.out.println("Informational: storeAll called");
        for (Map.Entry<Integer, String> entry : entries.entrySet()) {
            store(entry.getKey(), entry.getValue());
        }
    }

    public void delete(Integer key) {
        System.out.println("Informational: delete called");
        database.remove(key);
    }

    public void deleteAll(Collection<Integer> keys) {
        System.out.println("Informational: deleteAll called");
        for (Integer key : keys) {
            delete(key);
        }
    }

    public String load(Integer key) {
        System.out.println("Informational: load called");
        // Before returning the value we want to sleep to
        // simulate a slow database
        sleep(250);

        // Create the value and return it from the load method
        return database.select(key);
    }

    public Map<Integer, String> loadAll(Collection<Integer> keys) {
        System.out.println("Informational: loadAll called");
        Map<Integer, String> result = new HashMap<>();
        for (Integer key : keys) {
            result.put(key, load(key));
        }
        return result;
    }

    public Iterable<Integer> loadAllKeys() {
        System.out.println("Informational: loadAllKeys called");
        // Can be used to pre-populate known keys
        return Arrays.stream(database.selectKeys()).mapToObj(i -> i).collect(Collectors.toList());
    }

    private void sleep(long millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException ignore) {
        }
    }
}
