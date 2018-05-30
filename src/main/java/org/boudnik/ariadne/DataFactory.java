package org.boudnik.ariadne;

import org.apache.spark.sql.SparkSession;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * @author Alexandre_Boudnik
 * @since 05/23/2018
 */
public class DataFactory {
    private final Map<String, DataSource> dataSources = new HashMap<>();
    private final Map<Resource.Key, String> resources = new HashMap<>();
    private SparkSession spark = SparkSession
            .builder()
            .appName("Java Spark SQL basic example")
            .master("local[*]")
            .getOrCreate();

    public DataFactory(DataSource... dataSources) {
        for (DataSource dataSource : dataSources) {
            this.dataSources.put(dataSource.clazz.getName(), dataSource);
        }
    }

    public static Logger LOGGER;

    static {
        try (InputStream config = DataFactory.class.getClassLoader().getResourceAsStream("logging.properties")) {
            LogManager.getLogManager().readConfiguration(config);
            LOGGER = Logger.getLogger(DataFactory.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public <R> DataSource<R> getDataSource(String name) {
        //noinspection unchecked
        return dataSources.get(name);
    }

    public <R> String build(DataBlock<R> block) {
        for (Resource resource : block.ordered()) {
            resources.computeIfAbsent(resource.key(), k -> {
                try {
                    return resource.build(this);
                } catch (IOException | IllegalAccessException | NoSuchMethodException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            });
        }
        return get(block.key());
    }

    public String get(Resource.Key key) {
        return resources.get(key);
    }

    public SparkSession getSession() {
        return spark;
    }
}
