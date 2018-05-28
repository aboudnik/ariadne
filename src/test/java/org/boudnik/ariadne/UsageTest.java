package org.boudnik.ariadne;

import org.boudnik.ariadne.opsos.*;
import org.junit.Before;
import org.junit.Test;

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
        factory = new DataFactory(new HashMap<String, DataSource>() {{
            put(Hardware.class.getName(), new DataSource("file:///base/hardware/${state}/${city}"));
            put(Status.class.getName(), new DataSource("jdbc:postgresql://localhost/OPSOS/?select * status where state='${state}' and city = '${city}' and month = '${month}' and active=${operational}"));
            put(Traffic.class.getName(), new DataSource("file:///base/traffic/${state}/${month}"));
        }});
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

    @Test
    public void buildAndCache() {
        DataFactory.LOGGER.info("device = " + factory.build(usage));
        DataFactory.LOGGER.info("*");
        DataFactory.LOGGER.info("device = " + factory.build(usage));
    }

    @Test
    public void buildAndCachePartual() {
        DataFactory.LOGGER.info("device = " + factory.build(device));
        DataFactory.LOGGER.info("*");
        DataFactory.LOGGER.info("device = " + factory.build(usage));
    }

    @Test
    public void buildAndCachePartual1() throws NoSuchMethodException {
        System.out.println(usage.lambda().test(usage.record()));
    }
}
