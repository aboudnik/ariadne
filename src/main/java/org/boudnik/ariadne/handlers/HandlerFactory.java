package org.boudnik.ariadne.handlers;

import org.boudnik.ariadne.DataSource;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.nio.file.Paths;

/**
 * @author Sergey Nuyanzin
 * @since 5/28/2018
 */
public class HandlerFactory {
    public static final String FILE_PREFIX = "file:///";
    public static final String JDBC_PREFIX = "jdbc:";

    private HandlerFactory() {
    }

    private static class HandlerFactoryHolder {
        private static HandlerFactory INSTANCE = new HandlerFactory();
    }

    public static HandlerFactory getInstance() {
        return HandlerFactoryHolder.INSTANCE;
    }

    public Handler getHandler(String url) {
//        if (url.startsWith(FILE_PREFIX)) {
//            return new CsvFileHandler(new DataSource(FILE_PREFIX + Paths.get(".", url.substring(FILE_PREFIX.length())).toAbsolutePath().toString()));
//        } else if (url.startsWith(JDBC_PREFIX)) {
//            return new JdbcHandler();
//        }
        throw new UnsupportedOperationException("Not expected token " + url);
    }
}
