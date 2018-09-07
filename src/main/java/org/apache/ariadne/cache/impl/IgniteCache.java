package org.apache.ariadne.cache.impl;

import org.apache.ariadne.cache.TieredCache;
import org.apache.ignite.Ignition;

/**
 * @author Alexandre_Boudnik
 * @since 09/07/2018
 */
public class IgniteCache<K, V> extends AbstractTieredCache<K, V> {
    private final org.apache.ignite.IgniteCache<K, V> cache;

    public IgniteCache(String name, TieredCache<K, V> base) {
        super(base);
        cache = Ignition.ignite().getOrCreateCache(name);
    }

    @Override
    public V doGet(K key) {
        return cache.get(key);
    }

    @Override
    public void doPut(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public boolean doRemove(K key) {
        return cache.remove(key);
    }
}
