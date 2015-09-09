package com.hazelcast.training.caching.lab3;

import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.training.caching.common.cache.DistributedMapCache;
import com.hazelcast.training.caching.dto.Company;

import java.io.InputStream;

/**
 * TODO
 *
 * @author Viktor Gamov on 8/26/15.
 *         Twitter: @gamussa
 * @since 0.0.1
 */
public class CompanyCacheWithPersistenceReader {
    public static void main(String[] args) {
        final InputStream configInputStream =
            CompanyDistributedCacheWithPersistence.class.getClassLoader()
                .getResourceAsStream("hazelcast-lab3.xml");
        Config config = new XmlConfigBuilder(configInputStream).build();
        final HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(config);
        // Hazelcast.newHazelcastInstance();

        final DistributedMapCache<Integer, Company> companyCache =
            new DistributedMapCache<>(hazelcastInstance);
        System.out.println(companyCache.size());
        hazelcastInstance.shutdown();
    }

}
