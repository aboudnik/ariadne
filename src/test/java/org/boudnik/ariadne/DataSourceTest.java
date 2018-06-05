package org.boudnik.ariadne;

import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Alexandre_Boudnik
 * @since 05/22/2018
 */
public class DataSourceTest {

    private URL url;
    private String spec;

    @Before
    public void setUp() throws Exception {
        System.out.println(new File(".").getAbsolutePath());
        Path path = Paths.get("src", "test", "data", "opsos", "devices.csv");
        spec = "file:///" + path.toAbsolutePath().toString();
        url = new URL(spec);
    }

//    @Test
//    public void openFileStream() throws IOException {
//        DataSource dataSource = new DataSource(spec);
//        InputStream inputStream = dataSource.openStream();
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
//            for (String line; (line = reader.readLine()) != null; ) {
//                System.out.println("line = " + line);
//            }
//        }
//    }
}