package org.boudnik.ariadne;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author Alexandre_Boudnik
 * @since 05/04/2018
 */
public interface Resource extends Serializable {

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

    default void print() {
        walk(0,
                indent -> indent + 1,
                (resource, indent) -> {
                    for (int i = 0; i < indent; i++)
                        System.out.print(" ");
                    System.out.println(resource);
                },
                indent -> indent - 1);
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
        return walk(sink, x -> x, collector, x -> x);
    }

    default <C> C walk(C sink, Function<C, C> enter, BiConsumer<Resource, C> collector, Function<C, C> leave) {
        collector.accept(this, sink);
        for (Resource resource : prerequisites()) {
            resource.walk(enter.apply(sink), enter, collector, leave);
        }
        return leave.apply(sink);
    }

    Map<String, Object> dimensions();

    String build(DataFactory factory) throws IOException, IllegalAccessException, NoSuchMethodException;
}
