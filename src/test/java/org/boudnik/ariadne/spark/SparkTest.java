package org.boudnik.ariadne.spark;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.*;
import org.boudnik.ariadne.opsos.Traffic;
import org.junit.Test;

import java.net.URL;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.Properties;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * @author Alexandre_Boudnik
 * @since 05/29/2018
 */
public class SparkTest {

    private SparkSession spark = SparkSession
            .builder()
            .appName("Java Spark SQL basic example")
            .master("local[*]")
            .getOrCreate();


    @Test
    public <R> void first() {
        Date month = Date.valueOf("2018-02-01");
//        Dataset<Row> csv = spark.read().csv("C:/Projects/ariadne/src/test/data/opsos/traffic/*");
        Function<String, Traffic.Record> fromString = line -> {
            String[] parts = line.split(",");
            Traffic.Record traffic = new Traffic.Record();
            traffic.setDevice(Integer.valueOf(parts[0]));
            traffic.setPort(Integer.valueOf(parts[1]));
            traffic.setGigabytes(Double.valueOf(parts[2]));
            traffic.setMonth(month);
            return traffic;
        };
        JavaRDD<Traffic.Record> trafficRDD = spark.read()
                .textFile(Paths.get("src", "test", "data", "opsos", "traffic").toAbsolutePath().toString() + "/*")
                .javaRDD()
                .map(fromString)
                .filter((Function<Traffic.Record, Boolean>) record -> record.getDevice() == 101);
        Row row;
        Dataset<Traffic.Record> trafficDF = spark.createDataset(trafficRDD.rdd(), Encoders.bean(Traffic.Record.class));
        Dataset<Traffic.Record> trafficDF1 = spark.createDataset(trafficRDD.rdd(), Encoders.bean(Traffic.Record.class));

        trafficDF.createOrReplaceTempView("traffic");

        Dataset<Traffic.Record> sql = spark.sql("select * from traffic where gigabytes > 5").as(Encoders.bean(Traffic.Record.class));

        sql.show();

        sql.write().json("result");

        System.out.println();

        DataFrameReader read = spark.read();
        DataFrameWriter write = spark.sql("").write();

        Dataset<Row> jdbc = read.jdbc(" ", "table", new String[]{""}, new Properties());

//        Function<URL, Dataset<Row>> open = (r) -> {
//            return read.csv(r.toString());
//        };

    }

    BiFunction<SparkSession, URL, Dataset<Row>> open = (session, url) -> session.read().csv(url.toString());
    BiConsumer<Dataset<Row>, URL> save = (ds, url) -> ds.write().json(url.toString());
}
