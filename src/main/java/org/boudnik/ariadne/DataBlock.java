package org.boudnik.ariadne;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.mvel2.MVEL;
import org.mvel2.ParserContext;
import org.mvel2.templates.TemplateRuntime;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Alexandre_Boudnik
 * @author Sergey Nuyanzin
 * @since 05/23/2018
 */
public abstract class DataBlock<R extends Serializable> implements Resource {
    private final SortedMap<String, Object> dimensions = new TreeMap<>();
    private String alias;

    public final DataBlock<R> as(String alias) {
        this.alias = alias;
        return this;
    }

    public String table() {
        return alias == null ? Resource.super.table() : alias;
    }

    public DataBlock(Dimension... dimensions) {
        for (Dimension dimension : dimensions) {
            this.dimensions.put(dimension.name, dimension.limit);
        }
    }

    @Override
    public final Map<String, ?> dimensions() {
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
        for (Resource resource : prerequisites()) {
            Dataset<R> built = factory.build(resource);
            built.createOrReplaceTempView(resource.table());
        }
        Dataset<Row> sql = factory.getSession().sql(sql());
        JavaRDD<R> map = sql.javaRDD().map(this::valueOf);
        DataSource<R> dataSource = factory.getDataSource(type());
        Dataset<R> dataset = factory.getSession().createDataset(map.rdd(), Encoders.bean(dataSource.record));
        save(dataSource, dataset);
        return dataset;
    }

    private String eval(String template) {
        return (String) TemplateRuntime.eval(template, dimensions());
    }

    private Function<R, Boolean> getFilter() {
        Map<String, Object> vars = new HashMap<>();
        ParserContext context = new ParserContext();
        String and = "";
        StringBuilder sb = new StringBuilder();
        Set<String> fields = Stream.of((record().getClass().getDeclaredFields())).map(Field::getName).collect(Collectors.toSet());
        for (Map.Entry<String, ?> entry : dimensions().entrySet()) {
            if (!fields.contains(entry.getKey()))
                continue;
            String var = "_" + entry.getKey();
            vars.put(var, entry.getValue());
            context.withInput(var, entry.getValue().getClass());
            sb.append(and);
            and = " && ";
            sb.append(var).append(".equals(").append(entry.getKey()).append(")");
        }
        Serializable expression = MVEL.compileExpression(sb.toString(), context);
        return o -> (Boolean) MVEL.executeExpression(expression, o, vars);
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
