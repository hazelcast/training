package com.hazelcast.training.caching.lab1;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.training.caching.common.cache.DistributedMapCache;

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
}
