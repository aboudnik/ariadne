package org.apache.ariadne.spark;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;
import org.apache.ariadne.opsos.Traffic;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.nio.file.Paths;
import java.sql.Date;

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


    @Ignore("PoC - not for regular use")
    @Test
    public <R> void first() {
        Date month = Date.valueOf("2018-02-01");
        JavaRDD<Traffic.Record> trafficRDD = spark.read()
                .textFile(Paths.get("base", "external", "opsos", "traffic").toAbsolutePath().toString() + File.separator + "*")
                .javaRDD()
                .map(line -> {
                    String[] parts = line.split(",");
                    Traffic.Record traffic = new Traffic.Record();
                    traffic.setDevice(Integer.valueOf(parts[0]));
                    traffic.setPort(Integer.valueOf(parts[1]));
                    traffic.setGigabytes(Double.valueOf(parts[2]));
                    traffic.setMonth(month);
                    return traffic;
                })
                .filter((Function<Traffic.Record, Boolean>) record -> record.getDevice() == 101);
        Dataset<Traffic.Record> trafficDF = spark.createDataset(trafficRDD.rdd(), Encoders.bean(Traffic.Record.class));

        trafficDF.createOrReplaceTempView("traffic");

        Dataset<Traffic.Record> sql = spark.sql("select * from traffic where gigabytes > 5").as(Encoders.bean(Traffic.Record.class));

        sql.show();

        sql.write().json("result");

        System.out.println();

    }

}
