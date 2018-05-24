package org.boudnik.ariadne;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Sergey Nuyanzin
 * @since 5/24/2018
 */
//DimensionGenerator
public class DG {
    public static Dimension[] generate(Map<String, ?> dimensionsMap, Map<String, ?> limitationMap) {
        Dimension[] dimensions = new Dimension[limitationMap.size()];
        int i = 0;
        for (Map.Entry<String, ?> limitationMapEntry : limitationMap.entrySet()) {
            Object value = limitationMapEntry.getValue();
            Object dimensionLimit;
            dimensions[i] =
                    (value instanceof Supplier
                            && (dimensionLimit = ((Supplier) value).get()) instanceof String)
                            ? new Dimension(limitationMapEntry.getKey(), dimensionsMap.get(dimensionLimit))
                            : new Dimension(limitationMapEntry.getKey(), value);
            i++;
        }
        return dimensions;
    }
}
