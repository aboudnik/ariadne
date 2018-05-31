package org.boudnik.ariadne;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexandre_Boudnik
 * @since 05/23/2018
 */
public class DataFactory {
    private final Map<String, DataSource> dataSources = new HashMap<>();
    private final Map<Resource.Key, Dataset> resources = new HashMap<>();
    private final SparkSession spark = SparkSession
            .builder()
            .appName("Java Spark SQL basic example")
            .master("local[*]")
            .getOrCreate();

    public static Logger LOGGER;

    static {

        LOGGER = Logger.getLogger("org.boudnik.ariadne");
        LOGGER.setLevel(Level.DEBUG);
        Logger.getLogger("org.apache.spark").setLevel(Level.ERROR);
        Logger.getLogger("org.apache.hadoop").setLevel(Level.ERROR);
    }

    DataFactory(DataSource... dataSources) {
        for (DataSource dataSource : dataSources) {
            this.dataSources.put(dataSource.clazz.getName(), dataSource);
        }
    }

    <R> DataSource<R> getDataSource(String name) {
        //noinspection unchecked
        return dataSources.get(name);
    }

    <R> Dataset<R> build(Resource block) {
        Resource.Key key = block.key();
        Dataset dataset = resources.get(key);
        if (dataset == null) {
            DataFactory.LOGGER.info("BUILD " + key);
            resources.put(key, dataset = block.build(this));
        } else {
            DataFactory.LOGGER.info("REUSE " + key);
        }
        //noinspection unchecked
        return dataset;
    }

    SparkSession getSession() {
        return spark;
    }
}
