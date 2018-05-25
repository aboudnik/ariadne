package org.boudnik.ariadne;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.*;

/**
 * @author Alexandre_Boudnik
 * @since 05/23/2018
 */
public class DataFactory {
    private final Map<String, DataSource> dataSources;
    private final Map<Resource.Key, String> resources = new HashMap<>();

    public DataFactory(Map<String, DataSource> dataSources) {
        this.dataSources = dataSources;
    }

    public static Logger LOGGER;

    static {
        try(InputStream config = DataFactory.class.getClassLoader().getResourceAsStream("logging.properties")) {
            LogManager.getLogManager().readConfiguration(config);
            LOGGER = Logger.getLogger(DataFactory.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public DataSource getDataSource(String name) {
        return dataSources.get(name);
    }

    public <R> String build(DataBlock<R> block) {
        for (Resource resource : block.ordered()) {
            resources.computeIfAbsent(resource.key(), k -> resource.build(this));
        }
        return get(block.key());
    }

    public String get(Resource.Key key) {
        return resources.get(key);
    }
}
