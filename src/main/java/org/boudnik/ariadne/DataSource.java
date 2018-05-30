package org.boudnik.ariadne;

import org.apache.spark.sql.DataFrameReader;
import org.apache.spark.sql.DataFrameWriter;
import org.apache.spark.sql.Dataset;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * @author Alexandre_Boudnik
 * @since 05/22/2018
 */
public class DataSource<R> {
    final Class<?> clazz;
    final Class<R> record;
    final String src;
    final String dst;
    final BiFunction<DataFrameReader, String, Dataset<String>> open;
    final BiConsumer<DataFrameWriter<R>, String> save;

    public DataSource(Class<?> clazz, Class<R> record, String src, String dst, BiFunction<DataFrameReader, String, Dataset<String>> open, final BiConsumer<DataFrameWriter<R>, String> save) {
        this.clazz = clazz;
        this.record = record;
        this.src = src;
        this.dst = dst;
        this.save = save;
        this.open = open;
    }

    public InputStream openStream() throws IOException {
        return new URL(src).openStream();
    }
}
