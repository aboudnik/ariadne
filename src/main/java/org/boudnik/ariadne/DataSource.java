package org.boudnik.ariadne;

import org.apache.spark.sql.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * @author Alexandre_Boudnik
 * @author Sergey Nuyanzin
 * @since 05/22/2018
 */
public class DataSource<R> {
    final Class<?> clazz;
    final Class<R> record;
    final String src;
    final String dst;
    final BiFunction<DataFrameReader, String, Dataset<Row>> open;
    final BiConsumer<DataFrameWriter<R>, String> save;

    public DataSource(Class<?> clazz, Class<R> record, Path src, Path dst, BiFunction<DataFrameReader, String, Dataset<Row>> open, final BiConsumer<DataFrameWriter<R>, String> save) {
        this.clazz = clazz;
        this.record = record;
        this.src = src.toAbsolutePath().toString();
        this.dst = dst.toAbsolutePath().toString();
        this.save = save;
        this.open = open;
    }

    public InputStream openStream() throws IOException {
        return new URL(src).openStream();
    }
}
