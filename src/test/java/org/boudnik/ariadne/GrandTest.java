package org.boudnik.ariadne;

import org.apache.spark.sql.DataFrameReader;
import org.apache.spark.sql.DataFrameWriter;
import org.boudnik.ariadne.opsos.Traffic;
import org.h2.tools.Server;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
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
            new Dimension("state", "VA"),
            new Dimension("gigabytes", 11.2),
            new Dimension("port", 80)
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
            rmRF(Paths.get(CACHE));
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
    private static void rmRF(Path directory) throws IOException {
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    @Before
    public void setUp() {
        factory = new DataFactory(
                new DataSource<>(
                        Traffic.class,
                        Traffic.Record.class,
                        Paths.get(EXTERNAL, "opsos", "traffic", "${month}.${state}.csv").toAbsolutePath().toString(),
                        Paths.get(CACHE, "traffic", "${month}", "${state}").toAbsolutePath().toString(),
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
