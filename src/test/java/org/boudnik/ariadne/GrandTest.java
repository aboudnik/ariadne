package org.boudnik.ariadne;


import org.apache.spark.sql.DataFrameReader;
import org.apache.spark.sql.DataFrameWriter;
import org.apache.spark.sql.Dataset;
import org.boudnik.ariadne.opsos.Hardware;
import org.boudnik.ariadne.opsos.Total;
import org.boudnik.ariadne.opsos.Traffic;
import org.h2.tools.Server;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.nio.file.Paths;
import java.sql.*;

import static junit.framework.TestCase.assertEquals;

/**
 * @author Alexandre_Boudnik
 * @since 05/29/2018
 */
public class GrandTest {

    private static Server server;
    private Traffic traffic = new Traffic(
            new Dimension("month", Date.valueOf("2018-01-01")),
            new Dimension("state", "VA"),
            new Dimension("port", 80)
    );
    private Hardware hardware = new Hardware(
            new Dimension("state", "VA")
    );
    private Total total = new Total(
            new Dimension("month", Date.valueOf("2018-01-01")),
            new Dimension("state", "VA")
    );

    private DataFactory factory;
    private static String EXTERNAL;
    private static String CACHE;

    @BeforeClass
    public static void start() {
        try {
            server = Server.createTcpServer().start();
            String userDirProperty = System.getProperty("user.dir");
            EXTERNAL = Paths.get(userDirProperty, "base", "external").toAbsolutePath().toString();
            CACHE = Paths.get(userDirProperty, "base", "cache").toAbsolutePath().toString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
    private static void rmRF(File root) {
        if (root.isDirectory())
            for (File file : root.listFiles())
                rmRF(file);
        root.delete();
    }

    @Before
    public void setUp() {
        rmRF(new File(CACHE));
        factory = new DataFactory(
                new DataSource<>(
                        Traffic.class,
                        Traffic.Record.class,
                        Paths.get(EXTERNAL, "opsos", "traffic", "${month}.${state}.csv"),
                        Paths.get(CACHE, "traffic", "${month}", "${state}"),
                        DataFrameReader::csv,
                        DataFrameWriter::csv
                ),
                new DataSource<>(
                        Hardware.class,
                        Hardware.Record.class,
                        Paths.get(EXTERNAL, "opsos", "devices.csv"),
                        Paths.get(CACHE, "devices", "${state}"),
                        DataFrameReader::csv,
                        DataFrameWriter::json
                ),
                new DataSource<>(
                        Total.class,
                        Total.Record.class,
                        Paths.get(CACHE, "total", "${month}"),
                        Paths.get(CACHE, "total", "${month}"),
                        DataFrameReader::csv,
                        DataFrameWriter::csv
                )
        );
    }

    @Test
    public void traffic() {
        factory.build(traffic);
    }

    @Test
    public void hardware() {
        factory.build(hardware);
    }

    @Test
    public void hardware2() {
        factory.build(hardware);
        factory.build(hardware);
    }

    @Test
    public void total() {
        factory.build(total);
    }

    @Test
    public void testH2() {
        try {
            Class.forName("org.h2.Driver");
            try (Connection connection = DriverManager.getConnection("jdbc:h2:" + server.getURL() + "/~/DB", "sa", "sa")) {
                try (PreparedStatement statement = connection.prepareStatement("select * from information_schema.users")) {
                    try (ResultSet resultSet = statement.executeQuery()) {
                        while (resultSet.next()) {
                            assertEquals("SA", resultSet.getObject(1));
                        }
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void stop() {
        server.stop();
    }
}
