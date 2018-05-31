package org.boudnik.ariadne;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.boudnik.ariadne.handlers.SerializablePredicate;
import org.mvel2.templates.TemplateRuntime;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author Alexandre_Boudnik
 * @since 05/23/2018
 */
public abstract class DataBlock<R extends Serializable> implements Resource {
    private final SortedMap<String, Object> dimensions = new TreeMap<>();
    private String alias;

    public final DataBlock<R> as(String alias) {
        this.alias = alias;
        return this;
    }

    public String type() {
        return alias == null ? Resource.super.type() : alias;
    }

    public DataBlock(Dimension... dimensions) {
        for (Dimension dimension : dimensions) {
            this.dimensions().put(dimension.name, dimension.limit);
        }
    }

    @Override
    public final Map<String, Object> dimensions() {
        return dimensions;
    }

    @Override
    public String toString() {
        return "{" + type() + " " + dimensions + "}";
    }

    public abstract R record();

    public abstract String sql();

    public abstract R valueOf(Row row);

    @Override
    public Dataset<R> build(DataFactory factory) {
//        DataFactory.LOGGER.fine((factory.get(key()) == null ? "BUILD " : "----- ") + key());
        for (Resource resource : prerequisites()) {
            Dataset<R> built = factory.build(resource);
            String table = resource.type().replace(".", "_");
            built.createOrReplaceTempView(table);
        }
        Dataset<Row> sql = factory.getSession().sql(sql());
        JavaRDD<R> map = sql.javaRDD().map(this::valueOf);
        DataSource<R> dataSource = factory.getDataSource(type());
        Dataset<R> dataset = factory.getSession().createDataset(map.rdd(), Encoders.bean(dataSource.record));
        save(dataSource, dataset);
        return dataset;
    }

    public SerializablePredicate<R> lambda() throws NoSuchMethodException {
        @SuppressWarnings("unchecked") SerializablePredicate<R> predicate = SerializablePredicate.TRUE_PREDICATE;
        Class clazz = getClass().getDeclaredMethod("record").getReturnType();
        Map<String, Field> fieldMap = FieldsCache.getInstance().getFieldsMap(clazz);
        for (Map.Entry<String, ?> dimension : dimensions().entrySet()) {

            DataFactory.LOGGER.fine(dimension.getKey() + " " + dimension.getValue());
            Field field;
            if ((field = fieldMap.get(dimension.getKey())) == null) {
                continue;
            }
            predicate = predicate.and(o -> {
                try {
                    DataFactory.LOGGER.fine("res " + field.getDeclaringClass().getCanonicalName()
                            + " " + field.getName() + " " + dimension.getValue() + " to check  " + o);

                    return Objects.equals(o.getClass(), clazz) && Objects.equals(field.get(o), dimension.getValue());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            });
        }
        return predicate;
    }

    private String eval(String template) {
        return (String) TemplateRuntime.eval(template, dimensions());
    }

    private Function<R, Boolean> getFilter() {
        Function<R, Boolean> p = t -> true;
        Class clazz = record().getClass();
        for (Map.Entry<String, Field> entry : FieldsCache.getInstance().getFieldsMap(clazz).entrySet()) {
            Object expected = dimensions().get(entry.getKey());
            if (expected != null) {
                String name = entry.getValue().getName();
                Function<R, Boolean> finalP = p;
                p = t -> finalP.call(t) && Objects.equals(expected, clazz.getField(name).get(t));
            }
        }
        return p;
    }

    Dataset<R> save(DataSource<R> dataSource, Dataset<R> dataset) {
        String dst = eval(dataSource.dst);
        dataSource.save.accept(dataset.write(), dst);
        return dataset;
    }

    Dataset<R> load(DataSource<R> dataSource, SparkSession session) {
        Dataset<Row> input = dataSource.open.apply(session.read(), eval(dataSource.src));
        RDD<R> rdd = input.javaRDD().map(this::valueOf).filter(getFilter()).rdd();
        return session.createDataset(rdd, Encoders.bean(dataSource.record));
    }

}
