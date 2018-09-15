package org.apache.ariadne.cache;

import org.apache.log4j.Logger;

/**
 * It implements the abilities to:
 * <li>store key-value pairs, each referred to as an Cache.Entry
 * <li>allow use of Java Generics to improve application type-safety
 * <li>read-through-caching
 *
 * @author Alexandre_Boudnik
 * @since 09/07/2018
 */
public interface TieredCache<K, V> {

    Logger LOGGER = Logger.getLogger("org.apache.ariadne");

    /**
     * Gets an entry from the cache.
     *
     * @param key the key whose associated value is to be returned
     * @return the element, or null, if it does not exist.
     * @see javax.cache.Cache#get(Object)
     */
    default V get(K key) {
        V v = doGet(key);
        if (v == null) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("read through for " + key);
            return base() == null ? null : base().get(key);
        } else return v;
    }

    /**
     * Associates the specified value with the specified key in the cache.
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @see javax.cache.Cache#put(Object, Object)
     */
    default void put(K key, V value) {
        doPut(key, value);
    }

    /**
     * Removes the mapping for a key from this cache if it is present.
     *
     * @param key key whose mapping is to be removed from the cache
     * @return false if there was no matching key
     * @see javax.cache.Cache#remove(Object)
     */
    default boolean remove(K key) {
        return doRemove(key);
    }

    V doGet(K key);

    void doPut(K key, V value);

    boolean doRemove(K key);

    TieredCache<K, V> base();
}
