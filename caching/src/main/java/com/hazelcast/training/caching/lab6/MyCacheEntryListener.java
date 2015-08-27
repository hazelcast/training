package com.hazelcast.training.caching.lab6;

import com.hazelcast.training.caching.dto.Company;

import javax.cache.event.CacheEntryCreatedListener;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryListenerException;
import javax.cache.event.CacheEntryUpdatedListener;

/**
 * Example of an Created Entry Listener
 */
public class MyCacheEntryListener<I, C> implements CacheEntryCreatedListener<Integer, Company>,
    CacheEntryUpdatedListener<Integer, Company> {

    public void onCreated(
        Iterable<CacheEntryEvent<? extends Integer, ? extends Company>> cacheEntryEvents)
        throws CacheEntryListenerException {
        for (CacheEntryEvent entryEvent : cacheEntryEvents) {
            System.out.println(
                "Created : " + entryEvent.getKey() + " with value : " + entryEvent.getValue());
        }
    }

    public void onUpdated(
        Iterable<CacheEntryEvent<? extends Integer, ? extends Company>> cacheEntryEvents)
        throws CacheEntryListenerException {
        for (CacheEntryEvent entryEvent : cacheEntryEvents) {
            System.out.println(
                "Updated : " + entryEvent.getKey() + " with value : " + entryEvent.getValue());
        }
    }
}
