package org.boudnik.ariadne;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexandre_Boudnik
 * @since 05/23/2018
 */
public abstract class DataBlock<R> implements Resource {
    private final Map<String, Object> dimensions = new HashMap<>();

    public DataBlock(Dimension... dimensions) {
        for (Dimension dimension : dimensions) {
            this.dimensions().put(dimension.name, dimension.limit);
        }
    }

    @Override
    public final Map<String, Object> dimensions() {
        return dimensions;
    }

    @Override
    public String toString() {
        return "{" + getClass().getSimpleName() + " " + dimensions + "}";
    }

    protected abstract R record();
}
