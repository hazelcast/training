package com.hazelcast.training.caching.lab1;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.training.caching.common.ICache;
import com.hazelcast.training.caching.dto.Associate;
import com.hazelcast.training.caching.dto.Company;

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
public class CompanyDistributedCache {
    public static void main(String[] args) {
        final HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
        // Hazelcast.newHazelcastInstance();
        List<Associate> hazelcastAssociates = new ArrayList<>();
        List<Associate> abcxyzAssociates = new ArrayList<>();

        final DistributedMapCache<Integer, Company> companies =
            new DistributedMapCache<>(hazelcastInstance);

        hazelcastAssociates.add(new Associate("Fuad Malikov"));
        hazelcastAssociates.add(new Associate("Talip Ozturk"));
        hazelcastAssociates.add(new Associate("Viktor Gamov"));

        Company hazelcast = new Company("Hazelcast");
        hazelcast.setAssociates(hazelcastAssociates);
        companies.put(1, hazelcast);

        abcxyzAssociates.add(new Associate("Larry Page"));
        abcxyzAssociates.add(new Associate("Sergey Brin"));

        Company google = new Company("Google");
        google.setAssociates(abcxyzAssociates);
        companies.putIfAbsent(2, google);

        System.out.println("Companies count: " + companies.size());
        hazelcastInstance.shutdown();
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
