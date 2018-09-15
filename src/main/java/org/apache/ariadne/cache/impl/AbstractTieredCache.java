package org.apache.ariadne.cache.impl;

import org.apache.ariadne.cache.TieredCache;

import java.util.UUID;

/**
 * @author Alexandre_Boudnik
 * @since 09/07/2018
 */
public abstract class AbstractTieredCache<K, V> implements TieredCache<K, V> {
    private final TieredCache<K, V> base;

    /**
     * UUID v5 - namespace DNS - "org.apache.ariadne.dead"
     */
    private static final UUID TOMBSTONE = UUID.fromString("cf933292-d44f-5bd3-84c3-e2dd37a4f46a");

    AbstractTieredCache(TieredCache<K, V> base) {
        this.base = base;
    }

    @Override
    public TieredCache<K, V> base() {
        return base;
    }
}
