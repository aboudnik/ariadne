package org.boudnik.ariadne.handlers;

import org.boudnik.ariadne.DataSource;

import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergey Nuyanzin
 * @since 5/28/2018
 */
public class HandlerFactory {
    private final Map<String, Map<String, Field>> cache = new HashMap<>();

    private HandlerFactory() {
    }

    private static class HandlerFactoryHolder {
        private static HandlerFactory INSTANCE = new HandlerFactory();
    }

    public static HandlerFactory getInstance(){
        return HandlerFactoryHolder.INSTANCE;
    }

    public Handler getHandler(String url) {
        if(url.startsWith("file:///base")) {
            return new CsvFileHandler(new DataSource("file:///" +Paths.get(".", url.substring(7)).toAbsolutePath().toString()));
        } else if(url.startsWith("jdbc:")) {
            return new JdbcHandler();
        }
        throw new UnsupportedOperationException("Not expected token " + url);
    }
}
