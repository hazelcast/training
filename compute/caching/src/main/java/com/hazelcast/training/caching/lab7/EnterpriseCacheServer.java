package com.hazelcast.training.caching.lab7;

import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.io.InputStream;

/**
 * A simple test of a cache.
 */
public class EnterpriseCacheServer {
    public static void main(String[] input) throws Exception {
        InputStream configInputStream =
            EnterpriseCacheServer.class.getResourceAsStream("/hazelcast-lab7-server.xml");
        Config cfg = new XmlConfigBuilder(configInputStream).build();

        HazelcastInstance instance = Hazelcast.newHazelcastInstance(cfg);
    }
}
