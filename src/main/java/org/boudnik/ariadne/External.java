package org.boudnik.ariadne;

import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;
import org.mvel2.templates.TemplateRuntime;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
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
    public String build(DataFactory factory) {
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

    private Predicate<R> getFilter() {
        Predicate<R> p = getTrueFilter();
        Class clazz = record().getClass();
        System.out.println("clazz " + clazz);

        for (Map.Entry<String, Field> mapEntryField : FieldsCache.getInstance().getFieldsMap(clazz).entrySet()) {
            Object dimValue;
            if ((dimValue = dimensions().get(mapEntryField.getKey())) != null) {
                p = p.and(e -> {
                    try {
                        return Objects.equals(dimValue, mapEntryField.getValue().get(e));
                    } catch (Exception e1) {
                        throw new RuntimeException(e1);
                    }
                });
            }
        }
        return p;
    }

    private Predicate<R> getTrueFilter() {
        return t -> true;
    }
}
