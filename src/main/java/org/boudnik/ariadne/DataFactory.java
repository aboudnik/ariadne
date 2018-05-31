package org.boudnik.ariadne;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;

import java.io.IOException;
import java.io.InputStream;
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
    private final Map<Resource.Key, Dataset> resources = new HashMap<>();
    private SparkSession spark = SparkSession
            .builder()
            .appName("Java Spark SQL basic example")
            .master("local[*]")
            .getOrCreate();

    DataFactory(DataSource... dataSources) {
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

    <R> DataSource<R> getDataSource(String name) {
        //noinspection unchecked
        return dataSources.get(name);
    }

    <R> Dataset<R> build(Resource block) {
        Resource.Key key = block.key();
        Dataset dataset = resources.get(key);
        if(dataset==null) {
            resources.put(key, dataset = block.build(this));
        }
        //noinspection unchecked
        return dataset;
    }

    SparkSession getSession() {
        return spark;
    }
}
