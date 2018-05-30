package org.boudnik.ariadne;

import org.apache.spark.sql.DataFrameReader;
import org.apache.spark.sql.DataFrameWriter;
import org.boudnik.ariadne.opsos.Traffic;
import org.h2.tools.Server;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.sql.*;

import static junit.framework.TestCase.assertEquals;

/**
 * @author Alexandre_Boudnik
 * @since 05/29/2018
 */
public class GrandTest {

    private static Server server;
    Traffic traffic = new Traffic(
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
            File root = new File(System.getProperty("user.dir"), "base");
            EXTERNAL = new File(root, "external").toURI().getPath();
            CACHE = new File(root, "cache").toURI().getPath();
            rmRF(new File(CACHE));
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
        factory = new DataFactory(
                new DataSource<>(
                        Traffic.class,
                        Traffic.Record.class,
                        EXTERNAL + "opsos/traffic/${month}.${state}.csv",
                        CACHE + "traffic/${month}/${state}",
                        DataFrameReader::textFile,
                        DataFrameWriter::json
                ));
    }

    @Test
    public void traffic() {
        String traffic = factory.build(this.traffic);
        System.out.println("traffic = " + traffic);
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
