package com.hazelcast.training.caching.lab6;

import com.hazelcast.training.caching.dto.Company;

import javax.cache.configuration.Factory;
import javax.cache.event.CacheEntryListener;

/**
 * Insert and Update listener for JCache example app
 *
 * @author Viktor Gamov on 8/26/15.
 *         Twitter: @gamussa
 * @since 0.0.1
 */
public class MyEntryListenerFactory implements Factory<CacheEntryListener<Integer, Company>> {
    @Override public CacheEntryListener<Integer, Company> create() {
        return new MyCacheEntryListener<>();
    }
}
