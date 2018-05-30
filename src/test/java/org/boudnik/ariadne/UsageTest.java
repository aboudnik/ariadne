package org.boudnik.ariadne;

import org.boudnik.ariadne.opsos.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * @author Alexandre_Boudnik
 * @since 05/18/2018
 */
public class UsageTest {

    private Usage usage;
    private Device device;
    private DataFactory factory;

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

   /* @Test
    public void buildAndCache() {
        DataFactory.LOGGER.info("device = " + factory.build(usage));
        DataFactory.LOGGER.info("*");
        DataFactory.LOGGER.info("device = " + factory.build(usage));
    }*/

/*    @Test
    public void buildAndCachePartual() {
        DataFactory.LOGGER.info("device = " + factory.build(device));
        DataFactory.LOGGER.info("*");
        DataFactory.LOGGER.info("device = " + factory.build(usage));
    }*/

    @Test
    public void buildAndCachePartual1() throws NoSuchMethodException {
        Collection<Usage.Record> records = new ArrayList<Usage.Record>() {{
            add(usage.record("Leesburg", "VA", "2018-01-01"));
            add(usage.record("Leesburg", "VA", "2018-01-01"));
            add(usage.record("Leesburg", "VA", "2018-01-01"));
            add(usage.record("Leesburg", "VA1", "2018-01-02"));
            add(usage.record("Leesburg", "VA", "2018-01-01"));
            add(usage.record("Leesbu4rg", "VA", "2018-03-01"));
            add(usage.record("Leesburg", "VA", "2018-01-01"));
            add(usage.record("Lee4sburg", "VA", "2018-01-01"));
        }};
        DataFactory.LOGGER.info(String.valueOf(records.stream().filter(usage.lambda()).count()));
    }
}
