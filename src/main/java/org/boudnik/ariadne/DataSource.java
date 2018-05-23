package org.boudnik.ariadne;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author Alexandre_Boudnik
 * @since 05/22/2018
 */
public class DataSource {
    private final URL url;

    public DataSource(URL url) {
        this.url = url;
    }

    public InputStream openStream() throws IOException {
        return url.openStream();
    }
}
