package com.hazelcast.training.caching.common.cache;

/**
 * TODO
 *
 * @author Viktor Gamov on 8/25/15.
 *         Twitter: @gamussa
 * @since 0.0.1
 */
public interface MyCache<K, V> {
    void put(K key, V value);

    V get(K key);

    long size();
}
