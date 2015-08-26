package com.hazelcast.training.caching.lab2;

import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.training.caching.common.ICache;
import com.hazelcast.training.caching.dto.Associate;
import com.hazelcast.training.caching.dto.Company;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.hazelcast.training.caching.common.LabConstants.IMAP_NAME;

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
        List<Associate> hazelcastAssociates = new ArrayList<>();
        List<Associate> abcxyzAssociates = new ArrayList<>();

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


class DistributedMapCache<Integer, Company> implements ICache<Integer, Company> {
    private final HazelcastInstance hazelcastInstance;
    private final IMap<Integer, Company> companiesMaps;

    public DistributedMapCache(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
        this.companiesMaps = hazelcastInstance.getMap(IMAP_NAME);
    }

    @Override public void put(Integer key, Company value) {
        companiesMaps.set(key, value);
    }

    @Override public Company get(Integer key) {
        return companiesMaps.get(key);
    }

    public Company putIfAbsent(Integer key, Company value) {
        return companiesMaps.putIfAbsent(key, value);
    }

    @Override public long size() {
        return companiesMaps.size();
    }
}
