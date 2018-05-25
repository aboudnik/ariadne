package org.boudnik.ariadne;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author Alexandre_Boudnik
 * @since 05/22/2018
 */
public class DataSource {
    private final String template;

    public DataSource(String template) {
        this.template = template;
    }

    public InputStream openStream() throws IOException {
        return new URL(template).openStream();
    }

    public String getTemplate() {
        return template;
    }
}
