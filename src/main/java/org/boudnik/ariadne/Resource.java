package org.boudnik.ariadne;

import java.util.Set;

/**
 * @author Alexandre_Boudnik
 * @since 05/04/2018
 */
public interface Resource<DATA> {

    String type();

    /**
     * Supposed to build this
     */
    default void build(Loader<DATA> loader) {
    }

    <R extends Resource> Set<R> prerequisites();


    boolean isReady();

    default boolean isSatisfied() {
        boolean ready = true;
        for (Resource resource : prerequisites()) {
            ready &= resource.isReady();
        }
        return ready;
    }
}
