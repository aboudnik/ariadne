package org.apache.ariadne.cache.impl;

import org.apache.ariadne.cache.TieredCache;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexandre_Boudnik
 * @since 09/07/2018
 */
public class HashMapCache<K, V> extends AbstractTieredCache<K, V> {

    private Map<K, V> map = new HashMap<>();

    public HashMapCache(TieredCache<K, V> base) {
        super(base);
    }

    @Override
    public V doGet(K key) {
        return map.get(key);
    }

    @Override
    public void doPut(K key, V value) {
        map.put(key, value);
    }

    @Override
    public boolean doRemove(K key) {
        return null != map.remove(key);
    }
}
