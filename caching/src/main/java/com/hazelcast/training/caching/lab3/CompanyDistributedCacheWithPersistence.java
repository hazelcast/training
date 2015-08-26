package com.hazelcast.training.caching.lab3;

import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.training.caching.common.ICache;
import com.hazelcast.training.caching.dto.Company;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static com.hazelcast.training.caching.common.LabConstants.IMAP_NAME;

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


class DistributedMapCache<Integer, Company> implements ICache<Integer, Company> {
    private final HazelcastInstance hazelcastInstance;
    private final IMap<Integer, Company> companiesMap;

    public IMap<Integer, Company> getCompaniesMap() {
        return companiesMap;
    }

    public DistributedMapCache(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
        this.companiesMap = hazelcastInstance.getMap(IMAP_NAME);
    }

    @Override public void put(Integer key, Company value) {
        companiesMap.set(key, value);
    }

    @Override public Company get(Integer key) {
        return companiesMap.get(key);
    }

    public Company putIfAbsent(Integer key, Company value) {
        return companiesMap.putIfAbsent(key, value);
    }

    @Override public long size() {
        return companiesMap.size();
    }
}
