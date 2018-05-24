package org.boudnik.ariadne;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexandre_Boudnik
 * @since 05/23/2018
 */
public class Environment {
    final Map<String, DataSource> dataSources = new HashMap<>();

    DataSource getDataSource(String name) {
        return dataSources.get(name);
    }

    <R> void x(DataBlock<R> block) {

    }
}
