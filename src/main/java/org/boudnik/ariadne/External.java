package org.boudnik.ariadne;

import org.apache.hadoop.io.BooleanWritable;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;
import org.mvel2.templates.TemplateRuntime;
import scala.tools.nsc.Global;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * @author Alexandre_Boudnik
 * @since 05/24/2018
 */
public abstract class External<R> extends DataBlock<R> {
    public External(Dimension... dimensions) {
        super(dimensions);
    }

    public abstract R valueOf(String line);

    @Override
    public String build(DataFactory factory) throws IOException, IllegalAccessException, NoSuchMethodException {
        DataFactory.LOGGER.fine("LOAD  " + key());
        DataSource<R> dataSource = factory.getDataSource(type());
        SparkSession session = factory.getSession();
        String src = eval(dataSource.src);
        String dst = eval(dataSource.dst);
        RDD<R> rdd = dataSource.open.apply(session.read(), src).javaRDD().map(this::valueOf).filter(r -> getFilter().test(r)).rdd();
        Dataset<R> dataset = session.createDataset(rdd, Encoders.bean(dataSource.record));
        dataSource.save.accept(dataset.write(), dst);
        return dst;
    }

    private String eval(String template) {
        return (String) TemplateRuntime.eval(template, dimensions());
    }

//    private Predicate<R> getFilter() {
//        Predicate<R> p = t -> true;
//        Class clazz = record().getClass();
//        for (Map.Entry<String, Object> entry : dimensions().entrySet()) {
//            p = p.and(e -> {
//                try {
//                    return entry.getValue().equals(clazz.getField(entry.getKey()).get(e));
//                } catch (Exception e1) {
//                    throw new RuntimeException(e1);
//                }
//            });
//        }
//        return p;
//    }

    private Predicate<R> getFilter() {
        return t -> true;
    }
}
