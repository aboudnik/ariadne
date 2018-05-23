package org.boudnik.ariadne;

import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * @author Alexandre_Boudnik
 * @since 05/04/2018
 */
public interface Resource<DATA> {

    default String type() {
        return getClass().getName();
    }

    /**
     * Supposed to build this
     */
    default void build(Loader<DATA> loader) {
    }

    default Set<? extends Resource> prerequisites() {
        return Collections.emptySet();
    }

    default Map<String, ?> dimensions() {
        throw new NoSuchElementException();
    };


    boolean isReady();

    default boolean isSatisfied() {
        boolean ready = true;
        for (Resource resource : prerequisites()) {
            ready &= resource.isReady();
        }
        return ready;
    }
}
