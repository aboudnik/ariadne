package org.apache.ariadne;

import org.apache.ariadne.opsos.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Alexandre_Boudnik
 * @since 05/18/2018
 */
public class UsageTest {

    private Usage usage;
    private Device device;
    private DataFactory factory;

    private static Usage.Record record(String city, String state, String month) {
        Usage.Record record = new Usage.Record();
        record.setCity(city);
        record.setState(state);
        record.setMonth(month);
        return record;
    }

    @Before
    public void setUp() {
        usage = new Usage(
                new Dimension("month", "2018-01-01"),
                new Dimension("state", "VA"),
                new Dimension("city", "Leesburg")
        );
        device = new Device(
                new Dimension("month", "2018-01-01"),
                new Dimension("state", "VA"),
                new Dimension("city", "Leesburg"),
                new Dimension("operational", true));


//        factory = new DataFactory(new HashMap<String, DataSource>() {{
//            put(Hardware.class.getName(), new DataSource("file:///base/devices.csv"));
//            put(Status.class.getName(), new DataSource("jdbc:postgresql://localhost/OPSOS/?select * status where state='${state}' and city = '${city}' and month = '${month}' and active=${operational}"));
//            put(Traffic.class.getName(), new DataSource("file:///base/traffic/${month}.${state}.csv"));
//        }});
//

    }

    @Ignore("Deprecated")
    @Test
    public void prerequisites() {
        System.out.println("usage = " + usage);
        System.out.println("usage.prerequisites() = " + usage.prerequisites());

        System.out.println("device.prerequisites() = " + device.prerequisites());
    }


    @Test
    public void satisfied() {
        System.out.println("satisfied = " + usage.isSatisfied());
    }

    @Test
    public void ordered() {
        System.out.println("ordered = " + usage.ordered());
    }

    @Test
    public void print() {
        usage.print();
    }

    @Ignore("Deprecated")
    @Test
    public void buildAndCache() {
        DataFactory.LOGGER.info("device = " + factory.build(usage));
        DataFactory.LOGGER.info("*");
        DataFactory.LOGGER.info("device = " + factory.build(usage));
    }

    @Ignore("Deprecated")
    @Test
    public void buildAndCachePartual() {
        DataFactory.LOGGER.info("device = " + factory.build(device));
        DataFactory.LOGGER.info("*");
        DataFactory.LOGGER.info("device = " + factory.build(usage));
    }
}
