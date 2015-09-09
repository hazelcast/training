package com.hazelcast.training.caching.common.cache;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import static com.hazelcast.training.caching.common.LabConstants.IMAP_NAME;

public class  DistributedMapCache<Integer, Company> implements MyCache<Integer, Company> {
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
