package org.boudnik.ariadne;


import org.apache.spark.sql.DataFrameReader;
import org.apache.spark.sql.DataFrameWriter;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
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
import java.util.Properties;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static junit.framework.TestCase.assertEquals;

/**
 * @author Alexandre_Boudnik
 * @since 05/29/2018
 */
public class GrandTest {

    private static final String HARDWARE_TABLE = "HARDWARE";
    private static final String HARDWARE_OUTPUT = "HARDWARE_OUTPUT";
    private static final String SA_USER = "sa";
    private static final String SA_PASSWORD = "sa";
    private static final Properties PROPERTIES = new Properties();

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
            PROPERTIES.setProperty("user", SA_USER);
            PROPERTIES.setProperty("password", SA_PASSWORD);
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
    public void testH2() throws SQLException {
        final String h2Url = "jdbc:h2:" + server.getURL() + "/~/DB";
        try {
            Class.forName("org.h2.Driver");
            try (Connection connection = DriverManager.getConnection(h2Url, SA_USER, SA_PASSWORD)) {

                try(Statement stmt = connection.createStatement()) {

                    stmt.execute("DROP TABLE IF EXISTS " + HARDWARE_TABLE);
                    stmt.execute("DROP TABLE IF EXISTS " + HARDWARE_OUTPUT);
                    stmt.execute("CREATE TABLE " + HARDWARE_TABLE + "(device int primary key, state varchar(255), city varchar(255))");
                    stmt.execute("INSERT INTO " + HARDWARE_TABLE + " (device, state, city) values (101, 'MD', 'Rockville')");
                    stmt.execute("INSERT INTO " + HARDWARE_TABLE + " (device, state, city) values (102, 'MD', 'Rockville')");
                    stmt.execute("INSERT INTO " + HARDWARE_TABLE + " (device, state, city) values (103, 'MD', 'Rockville')");
                    stmt.execute("INSERT INTO " + HARDWARE_TABLE + " (device, state, city) values (204, 'VA', 'Leesburg')");
                    stmt.execute("INSERT INTO " + HARDWARE_TABLE + " (device, state, city) values (205, 'VA', 'Leesburg')");
                    stmt.execute("INSERT INTO " + HARDWARE_TABLE + " (device, state, city) values (206, 'VA', 'Leesburg')");
                    stmt.execute("INSERT INTO " + HARDWARE_TABLE + " (device, state, city) values (207, 'VA', 'Leesburg')");

                    factory = new DataFactory(
                            new DataSource(
                                    Hardware.class,
                                    Hardware.Record.class,
                                    Paths.get(EXTERNAL, "opsos", "devices.csv"),
                                    Paths.get(CACHE, "devices", "${state}"),

                                    new BiFunction<DataFrameReader, String, Dataset<Row>>() {
                                        @Override
                                        public Dataset<Row> apply(DataFrameReader dataFrameReader, String s) {
                                            return dataFrameReader.jdbc(h2Url, HARDWARE_TABLE, PROPERTIES);
                                        }
                                    },
                                    new BiConsumer<DataFrameWriter, String>() {
                                        @Override
                                        public void accept(DataFrameWriter dataFrameWriter, String s) {
                                            dataFrameWriter.jdbc(h2Url, HARDWARE_OUTPUT, PROPERTIES);
                                        }
                                    }
                            )
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        Dataset<Hardware.Record> build = factory.build(hardware);

        try(Connection connection = DriverManager.getConnection(h2Url, SA_USER, SA_PASSWORD)){
            try {
                Statement stmt = connection.createStatement();
                String SelectQueryOutput = "select * from " + HARDWARE_OUTPUT;
                ResultSet rsOutput = stmt.executeQuery(SelectQueryOutput);
                while (rsOutput.next()) {
                    System.out.println("Device " + rsOutput.getInt("device") + " City "
                            + rsOutput.getString("city") + " State " + rsOutput.getString("state"));
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        System.out.println("hardware = " + hardware + " " + build);
    }

    @AfterClass
    public static void stop() {
        server.stop();
    }
}
