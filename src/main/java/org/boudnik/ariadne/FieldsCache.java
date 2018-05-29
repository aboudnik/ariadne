package org.boudnik.ariadne;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Sergey Nuyanzin
 * @since 5/25/2018
 */
public class FieldsCache {
    private final Map<String, Map<String, Field>> cache = new HashMap<>();

    private FieldsCache() {
    }

    private static class FieldsCacheHolder {
        private static FieldsCache INSTANCE = new FieldsCache();
    }

    public static FieldsCache getInstance() {
        return FieldsCacheHolder.INSTANCE;
    }

    public void put(Field field) {
        cache.putIfAbsent(field.getDeclaringClass().getCanonicalName(), new HashMap<>());
        cache.get(field.getDeclaringClass().getCanonicalName()).putIfAbsent(field.getName(), field);
    }

    public void put(Class clazz, Field... fields) {
        for (Field field : fields) {
            cache.putIfAbsent(clazz.getCanonicalName(), new HashMap<>());
            cache.get(clazz.getCanonicalName()).putIfAbsent(field.getName(), field);
        }
    }

    public Field getField(String canonicalClassName, String fieldName) {
        Map<String, Field> fieldsMap;
        if ((fieldsMap = cache.get(canonicalClassName)) == null) return null;
        return fieldsMap.get(fieldName);
    }


    public Map<String, Field> getFieldsMap(Class clazz) {
        String canonicalClassName = clazz.getCanonicalName();
        if (cache.get(canonicalClassName) != null) {
            return cache.get(canonicalClassName);
        } else {
            Map<String, Field> fieldMap =
                    Stream.of(clazz.getDeclaredFields()).collect(Collectors.toMap(Field::getName, Function.identity()));
            fieldMap.values().forEach(t -> t.setAccessible(true));
            cache.put(canonicalClassName, fieldMap);
        }
        return cache.get(canonicalClassName);
    }
}
