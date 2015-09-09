package com.hazelcast.training.caching.lab1;

import com.hazelcast.training.caching.common.cache.LocalCache;
import com.hazelcast.training.caching.common.cache.MyCache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * TODO
 *
 * @author Viktor Gamov on 8/24/15.
 *         Twitter: @gamussa
 * @since 0.0.1
 */
public class RunningLocalCache {
    public static void main(String[] args) {
        Application application = new Application(new LocalCache<>());
        application.run();
    }

    static class LocalCache<Integer, Company> implements MyCache<Integer, Company> {
        ConcurrentMap<Integer, Company> storage = new ConcurrentHashMap<>();


        @Override
        public void put(Integer key, Company value) {
            this.storage.put(key, value);
        }

        @Override
        public Company get(Integer key) {
            return this.storage.get(key);
        }

        @Override
        public long size() {
            return storage.size();
        }

        public Company putIfAbsent(Integer key, Company value) {
            return storage.putIfAbsent(key, value);
        }
    }
}