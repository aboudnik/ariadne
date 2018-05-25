package org.boudnik.ariadne;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author Alexandre_Boudnik
 * @since 05/23/2018
 */
public abstract class DataBlock<R> implements Resource {
    private final SortedMap<String, Object> dimensions = new TreeMap<>();
    private String alias;

    public final DataBlock<R> as(String alias) {
        this.alias = alias;
        return this;
    }

    public String type() {
        return alias == null ? Resource.super.type() : alias;
    }

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
        return "{" + type() + " " + dimensions + "}";
    }

    protected abstract R record();

    @Override
    public String build(DataFactory factory) {
        DataFactory.LOGGER.fine((factory.get(key()) == null ? "BUILD " : "----- ") + key());
        return "file:///ephemeral/" + type();
    }
}
