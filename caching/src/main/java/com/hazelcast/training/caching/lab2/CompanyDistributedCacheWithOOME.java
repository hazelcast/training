package com.hazelcast.training.caching.lab2;

import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.training.caching.dto.Company;
import com.hazelcast.training.caching.lab1.RunningDistributedCache.DistributedCache;

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
        Config config = loadConfig("hazelcast-lab2.xml");
        final HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(config);

        final DistributedCache<Integer, Company> companyCache =
                new DistributedCache<>(hazelcastInstance);

        Runtime runtime = Runtime.getRuntime();
        int counter = 0;
        while (true) {
            Company company = new Company("Company_" + counter);
            company.setPayload(new byte[2_000_000]);
            companyCache.put(counter, company);
            counter++;
            System.out.printf("Puts = %s keyCount : Free Memory = %s MB\n", counter,
                (runtime.freeMemory() / (1024 * 1024)));
        }

    }

    private static Config loadConfig(String configFileName) {
        final InputStream configInputStream = CompanyDistributedCacheWithOOME.class.getClassLoader()
                .getResourceAsStream(configFileName);
        return new XmlConfigBuilder(configInputStream).build();
    }
}
