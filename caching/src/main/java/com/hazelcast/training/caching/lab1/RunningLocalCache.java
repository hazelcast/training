package com.hazelcast.training.caching.lab1;

import com.hazelcast.training.caching.common.cache.LocalCache;

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
}


