package org.apache.ariadne.cache.impl;

import org.apache.ariadne.cache.TieredCache;

/**
 * @author Alexandre_Boudnik
 * @since 09/07/2018
 */
public abstract class AbstractTieredCache<K, V> implements TieredCache<K, V> {
    private final TieredCache<K, V> base;

    AbstractTieredCache(TieredCache<K, V> base) {
        this.base = base;
    }

    @Override
    public TieredCache<K, V> base() {
        return base;
    }
}
