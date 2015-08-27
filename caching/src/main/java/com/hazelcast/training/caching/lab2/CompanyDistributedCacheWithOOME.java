package com.hazelcast.training.caching.lab2;

import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.training.caching.common.cache.DistributedMapCache;
import com.hazelcast.training.caching.dto.Company;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * TODO
 *
 * @author Viktor Gamov on 8/24/15.
 *         Twitter: @gamussa
 * @since 0.0.1
 */
public class CompanyDistributedCacheWithOOME {
    public static void main(String[] args) throws FileNotFoundException {
        final InputStream configInputStream = CompanyDistributedCacheWithOOME.class.getClassLoader()
            .getResourceAsStream("hazelcast-lab2.xml");
        Config config = new XmlConfigBuilder(configInputStream).build();
        final HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(config);
        // Hazelcast.newHazelcastInstance();

        final DistributedMapCache<Integer, Company> companyCache =
            new DistributedMapCache<>(hazelcastInstance);

        String companyName = new String(new char[1000000]); // 2 MB
        Runtime runtime = Runtime.getRuntime();

        int keyCount = 0;
        int mb = 1024 * 1024;

        while (true) {
            companyCache.put(keyCount, new Company(companyName));
            keyCount++;
            System.out.printf("Unique Puts = %s keyCount : Free Memory (MB) = %s\n", keyCount,
                runtime.freeMemory() / mb);
        }
    }
}
