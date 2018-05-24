package org.boudnik.ariadne;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * @author Alexandre_Boudnik
 * @since 05/04/2018
 */
public interface Resource {

    class Key {
        String type;
        Map<String, Object> dimensions;

        Key(String type, Map<String, Object> dimensions) {
            this.type = type;
            this.dimensions = dimensions;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Key key = (Key) o;
            return Objects.equals(type, key.type) && Objects.equals(dimensions, key.dimensions);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, dimensions);
        }

        @Override
        public String toString() {
            return type + dimensions;
        }
    }

    default Key key() {
        return new Key(type(), dimensions());
    }

    default String type() {
        return getClass().getTypeName();
    }

    default void print(boolean trace) {
        print(trace ? 0 : -1);
    }

    default void print(int indent) {
        for (int i = 0; i < indent; i++)
            System.out.print(" ");
        if (indent >= 0)
            System.out.println("building " + this);
        for (Resource resource : prerequisites()) {
            resource.print(indent >= 0 ? indent + 1 : indent);
        }
    }

    default Set<? extends Resource> prerequisites() {
        return Collections.emptySet();
    }

    default boolean isReady() {
        return false;
    }

    default boolean isSatisfied() {
        return walk(new boolean[]{true}, (r, b) -> b[0] &= r.isReady())[0];
    }

    default List<Resource> ordered() {
        return walk(new ArrayList<>(), (r, sink) -> sink.add(0, r));
    }

    default <C> C walk(C sink, BiConsumer<Resource, C> collector) {
        collector.accept(this, sink);
        for (Resource resource : prerequisites()) {
            resource.walk(sink, collector);
        }
        return sink;
    }

    Map<String, Object> dimensions();

    String build(DataFactory dataFactory);
}
