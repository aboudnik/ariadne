package org.boudnik.ariadne;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Predicate;

/**
 * @author Alexandre_Boudnik
 * @since 05/23/2018
 */
public abstract class DataBlock<R> implements Resource {
    private static final Predicate PREDICATE = t -> true;
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

    public abstract R record();

    @Override
    public String build(DataFactory factory) throws IOException, IllegalAccessException, NoSuchMethodException {
        DataFactory.LOGGER.fine((factory.get(key()) == null ? "BUILD " : "----- ") + key());
        return "file:///ephemeral/" + type();
    }

    public Predicate<R> lambda() throws NoSuchMethodException {

        Predicate<R> predicate = PREDICATE;
        Class clazz = getClass().getDeclaredMethod("record").getReturnType();
        Map<String, Field> fieldMap = FieldsCache.getInstance().getFieldsMap(clazz);
        for (Map.Entry<String, ?> dimension : dimensions().entrySet()) {

            DataFactory.LOGGER.fine(dimension.getKey() + " " + dimension.getValue());
            Field field;
            if ((field = fieldMap.get(dimension.getKey())) == null) {
                continue;
            }
            predicate = predicate.and(o -> {
                try {
                    DataFactory.LOGGER.fine("res " + field.getDeclaringClass().getCanonicalName() + " " + field.getName() + " " + dimension.getValue() + " to check  " + o);

                    return Objects.equals(o.getClass(), clazz) && Objects.equals(field.get(o), dimension.getValue());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            });
        }
        return predicate;
    }
}
