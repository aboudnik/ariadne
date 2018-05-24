package org.boudnik.ariadne;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * @author Alexandre_Boudnik
 * @since 05/23/2018
 */
public class DataFactory {
    private final Map<String, DataSource> dataSources = new HashMap<>();
    private final Map<Resource.Key, String> resources = new HashMap<>();
    public Logger LOGGER = Logger.getLogger(getClass().getName());

    DataSource getDataSource(String name) {
        return dataSources.get(name);
    }

    public <R> String build(DataBlock<R> block) {
        for (Resource resource : block.ordered()) {
            Resource.Key key = resource.key();
            LOGGER.info(key + (resources.get(key) == null ? " - BUILD IT" : " - IT'S UP TO DATE"));
            resources.computeIfAbsent(key, k -> resource.build(this));
        }
        return resources.get(block.key());
    }
}
