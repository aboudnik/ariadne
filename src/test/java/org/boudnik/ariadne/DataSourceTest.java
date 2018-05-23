package org.boudnik.ariadne;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.*;

/**
 * @author Alexandre_Boudnik
 * @since 05/22/2018
 */
public class DataSourceTest {

    private URL url;

    @Before
    public void setUp() throws Exception {
        url = new URL("file:///Projects/ariadne/src/test/data/opsos/devices.csv");
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