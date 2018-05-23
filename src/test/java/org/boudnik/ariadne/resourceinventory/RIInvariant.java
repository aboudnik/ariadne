package org.boudnik.ariadne.resourceinventory;

import org.boudnik.ariadne.Resource;

import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

/**
 * @author Sergey Nuyanzin
 * @since 5/15/2018
 */
public class RIInvariant<T> implements Resource {
    private final Function<T, ?> function2getValue;
    private final Object value;

    public RIInvariant(Function<T, ?> function2getValue, Object value) {
        this.function2getValue = function2getValue;
        this.value = value;
    }

    public void build() {

    }

    public Function<T, ?> getFunction2getValue() {
        return function2getValue;
    }

    public Object getValue() {
        return value;
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
