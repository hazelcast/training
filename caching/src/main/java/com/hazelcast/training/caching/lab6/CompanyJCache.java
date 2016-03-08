package com.hazelcast.training.caching.lab6;


import com.hazelcast.cache.ICache;
import com.hazelcast.training.caching.dto.Associate;
import com.hazelcast.training.caching.dto.Company;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.*;
import javax.cache.event.CacheEntryEventFilter;
import javax.cache.processor.EntryProcessor;
import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.MutableEntry;
import javax.cache.spi.CachingProvider;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple JCache example application
 *
 * @author Viktor Gamov on 8/24/15.
 *         Twitter: @gamussa
 * @since 0.0.1
 */
public class CompanyJCache {
    private static final String CACHING_PROVIDER_HAZELCAST =
        "com.hazelcast.cache.HazelcastCachingProvider";

    public static final boolean IS_OLD_VALUE_REQUIRED = false;
    public static final boolean IS_SYNCHRONOUS = true;
    public static final Factory<? extends CacheEntryEventFilter> NO_FILTER = null;

    public static void main(String[] args) {
        List<Associate> hazelcastAssociates = new ArrayList<>();
        List<Associate> abcxyz = new ArrayList<>();

        System.setProperty("hazelcast.jcache.provider.type", "server");
        CachingProvider cachingProvider =
            Caching.getCachingProvider(CACHING_PROVIDER_HAZELCAST);

        for (CachingProvider provider : Caching.getCachingProviders()) {
            System.out.println("Caching provide" + provider);
        }

        // Acquire the default cache manager
        CacheManager manager = cachingProvider.getCacheManager();

        // Define a cache
        Configuration<Integer, Company> cacheConfig =
            new MutableConfiguration<Integer, Company>().setStoreByValue(true)
                .setTypes(Integer.class, Company.class);

        // Create the cache
        Cache<Integer, Company> companyCache = manager.createCache("company", cacheConfig);

        // Create entry listener configuration
        CacheEntryListenerConfiguration listenerConfiguration =
            new MutableCacheEntryListenerConfiguration<Integer, Company>(
                new MyEntryListenerFactory(), null, true, true);

        companyCache.registerCacheEntryListener(listenerConfiguration);

        hazelcastAssociates.add(new Associate("Fuad Malikov"));
        hazelcastAssociates.add(new Associate("Talip Ozturk"));
        hazelcastAssociates.add(new Associate("Viktor Gamov"));

        Company hazelcast = new Company("Hazelcast");
        hazelcast.setAssociates(hazelcastAssociates);
        companyCache.put(1, hazelcast);

        abcxyz.add(new Associate("Larry Page"));
        abcxyz.add(new Associate("Sergey Brin"));

        Company google = new Company("Google");
        google.setAssociates(abcxyz);
        companyCache.put(2, google);

        // execute EntryProcessor
        companyCache.invoke(2, new EntryProcessor<Integer, Company, Object>() {
            @Override
            public Object process(MutableEntry<Integer, Company> entry, Object... arguments)
                throws EntryProcessorException {
                if (entry.exists()) {
                    final Company value = entry.getValue();
                    value.setCompanyName("abc.xyz");
                    entry.setValue(value);
                }
                return null;
            }
        });

        System.out.println("Got Google? " + companyCache.get(2));
        final ICache iCache = companyCache.unwrap(ICache.class);

        //
        System.out.println("Companies count: " + iCache.size());
    }
}
