package com.hazelcast.training.caching.common.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LocalCache<Integer, Company> implements MyCache<Integer, Company> {
    ConcurrentMap<Integer, Company> storage = new ConcurrentHashMap<>();

    @Override public void put(Integer key, Company value) {
        this.storage.put(key, value);
    }

    @Override public Company get(Integer key) {
        return this.storage.get(key);
    }

    @Override public long size() {
        return storage.size();
    }

    public Company putIfAbsent(Integer key, Company value) {
        return storage.putIfAbsent(key, value);
    }
}
