package org.boudnik.ariadne.resourceinventory;

import org.boudnik.ariadne.Resource;

import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author Sergey Nuyanzin
 * @since 5/15/2018
 */
public class RICondition<T> implements Resource {
    private final Predicate<T> predicate;

    public RICondition(Predicate<T> predicate) {
        this.predicate = predicate;
    }

    @Override
    public String type() {
        return null;
    }

    public void build() {

    }

    public Predicate<T> getPredicate() {
        return predicate;
    }

    @Override
    public Set<Resource> prerequisites() {
        return Collections.singleton(this);
    }

    @Override
    public boolean isReady() {
        return true;
    }
}
