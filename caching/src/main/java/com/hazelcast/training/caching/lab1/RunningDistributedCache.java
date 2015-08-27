package com.hazelcast.training.caching.lab1;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.training.caching.common.LabConstants;
import com.hazelcast.training.caching.common.cache.DistributedMapCache;
import com.hazelcast.training.caching.common.cache.MyCache;

/**
 * TODO
 *
 * @author Viktor Gamov on 8/24/15.
 *         Twitter: @gamussa
 * @since 0.0.1
 */
public class RunningDistributedCache {
    public static void main(String[] args) {
        final HazelcastInstance hazelcast = Hazelcast.newHazelcastInstance();
        Application application = new Application(new DistributedMapCache<>(hazelcast));
        application.run();
    }

    public static class DistributedCache<Integer, Company> implements MyCache<Integer, Company> {
        private final HazelcastInstance hazelcastInstance;
        private final IMap<Integer, Company> companiesMaps;

        public DistributedCache(HazelcastInstance hazelcastInstance) {
            this.hazelcastInstance = hazelcastInstance;
            this.companiesMaps = hazelcastInstance.getMap(LabConstants.IMAP_NAME);
        }

        @Override
        public void put(Integer key, Company value) {
            companiesMaps.set(key, value);
        }

        @Override
        public Company get(Integer key) {
            return companiesMaps.get(key);
        }

        public Company putIfAbsent(Integer key, Company value) {
            return companiesMaps.putIfAbsent(key, value);
        }

        @Override
        public long size() {
            return companiesMaps.size();
        }
    }
}
