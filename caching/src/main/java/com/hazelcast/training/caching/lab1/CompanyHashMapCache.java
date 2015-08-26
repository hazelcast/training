package com.hazelcast.training.caching.lab1;

import com.hazelcast.training.caching.common.ICache;
import com.hazelcast.training.caching.dto.Associate;
import com.hazelcast.training.caching.dto.Company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author Viktor Gamov on 8/24/15.
 *         Twitter: @gamussa
 * @since 0.0.1
 */
public class CompanyHashMapCache {
    public static void main(String[] args) {
        List<Associate> hazelcastAssociates = new ArrayList<>();
        List<Associate> abcxyz = new ArrayList<>();

        ICache<Integer, Company> companies = new HashMapCache<>();

        hazelcastAssociates.add(new Associate("Fuad Malikov"));
        hazelcastAssociates.add(new Associate("Talip Ozturk"));
        hazelcastAssociates.add(new Associate("Viktor Gamov"));

        Company hazelcast = new Company("Hazelcast");
        hazelcast.setAssociates(hazelcastAssociates);
        companies.put(1, hazelcast);

        abcxyz.add(new Associate("Larry Page"));
        abcxyz.add(new Associate("Sergey Brin"));

        Company google = new Company("Google");
        google.setAssociates(abcxyz);
        companies.put(2, google);

        System.out.println("Companies count: " + companies.size());
    }

}


class HashMapCache<Integer, Company> implements ICache<Integer, Company> {
    Map<Integer, Company> storage = new HashMap<>();

    @Override public void put(Integer key, Company value) {
        this.storage.put(key, value);
    }

    @Override public Company get(Integer key) {
        return this.storage.get(key);
    }

    @Override public long size() {
        return storage.size();
    }
}
