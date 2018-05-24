package org.boudnik.ariadne;

import org.boudnik.ariadne.DataFactory;
import org.boudnik.ariadne.Dimension;
import org.boudnik.ariadne.opsos.Device;
import org.boudnik.ariadne.opsos.Usage;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Alexandre_Boudnik
 * @since 05/18/2018
 */
public class UsageTest {

    private Usage usage;
    private Device device;
    private DataFactory factory;

    @Before
    public void setUp() throws Exception {
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
        factory = new DataFactory();
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
        usage.print(0);
    }

    @Test
    public void buildAndCache() {
        factory.LOGGER.info("device = " + factory.build(usage));
        factory.LOGGER.info("*");
        factory.LOGGER.info("device = " + factory.build(usage));
    }

    @Test
    public void buildAndCachePartual() {
        factory.LOGGER.info("device = " + factory.build(device));
        factory.LOGGER.info("*");
        factory.LOGGER.info("device = " + factory.build(usage));
    }
}
