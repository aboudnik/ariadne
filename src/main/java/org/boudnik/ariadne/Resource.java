package org.boudnik.ariadne;

import java.util.Set;

/**
 * @author Alexandre_Boudnik
 * @since 05/04/2018
 */
public interface Resource {

    String type();

    /**
     * Supposed to build this
     */
    default void build() {

    }

    Set<Resource> prerequisites();

    boolean isReady();

    default boolean isSatisfied() {
        boolean ready = true;
        for (Resource resource : prerequisites()) {
            ready &= resource.isReady();
        }
        return ready;
    }
}
