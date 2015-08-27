package com.hazelcast.training.caching.lab3;

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
public class CompanyDistributedCacheWithPersistence {
    public static void main(String[] args) throws FileNotFoundException {
        final InputStream configInputStream = CompanyDistributedCacheWithPersistence.class.getClassLoader()
            .getResourceAsStream("hazelcast-lab3.xml");
        Config config = new XmlConfigBuilder(configInputStream).build();
        final HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(config);
        // Hazelcast.newHazelcastInstance();

        final DistributedMapCache<Integer, Company> companyCache =
            new DistributedMapCache<>(hazelcastInstance);

        Company hazelcast = new Company("Hazelcast");
        companyCache.put(1, hazelcast);

        Company google = new Company("Google");
        companyCache.putIfAbsent(2, google);

        System.out.println("Companies count: " + companyCache.size());

        companyCache.getCompaniesMap().evict(1);
        System.out.println("Companies count after eviction: " + companyCache.size());

        // read through cache
        final Company company = companyCache.get(1);
        System.out.println(company);
        System.out.println("Companies count after read through: " + companyCache.size());
        hazelcastInstance.shutdown();
    }
}


