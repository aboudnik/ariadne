package org.boudnik.ariadne;

import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * @author Alexandre_Boudnik
 * @since 05/22/2018
 */
public class DataSourceTest {

    private URL url;

    @Before
    public void setUp() throws Exception {
        System.out.println(new File(".").getAbsolutePath());
        Path path = Paths.get( "src", "test", "data", "opsos", "devices.csv");
        url = new URL("file:///" + path.toAbsolutePath().toString());
    }

    @Test
    public void openFileStream() throws IOException {
        DataSource dataSource = new DataSource(url);
        InputStream inputStream = dataSource.openStream();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            for (String line; (line = reader.readLine()) != null; ) {
                System.out.println("line = " + line);
            }
        }
    }
}