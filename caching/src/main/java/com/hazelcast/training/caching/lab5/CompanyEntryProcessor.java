package com.hazelcast.training.caching.lab5;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.AbstractEntryProcessor;
import com.hazelcast.training.caching.common.cache.DistributedMapCache;
import com.hazelcast.training.caching.dto.Associate;
import com.hazelcast.training.caching.dto.Company;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author Viktor Gamov on 8/26/15.
 *         Twitter: @gamussa
 * @since 0.0.1
 */
public class CompanyEntryProcessor {
    public static void main(String[] args) {
        final HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
        final DistributedMapCache<Integer, Company> companyCache =
            new DistributedMapCache<>(hazelcastInstance);

        List<Associate> googleAssociates = new ArrayList<>();
        googleAssociates.add(new Associate("Larry Page"));
        googleAssociates.add(new Associate("Sergey Brin"));
        Company google = new Company("Google");
        google.setAssociates(googleAssociates);
        companyCache.put(2, google);

        // naïve versions
        // final Company company = companyCache.get(2);
        // company.setCompanyName("new name");
        // companyCache.put(2, company);

        companyCache.getCompaniesMap()
            .executeOnKey(2, new AbstractEntryProcessor<Integer, Company>() {
                @Override public Object process(Map.Entry<Integer, Company> entry) {
                    final Company value = entry.getValue();
                    value.setCompanyName("abc.xyz");
                    entry.setValue(value);
                    return null;
                }
            });

        System.out.println("Got Google? " + companyCache.get(2));
        hazelcastInstance.shutdown();
    }
}
