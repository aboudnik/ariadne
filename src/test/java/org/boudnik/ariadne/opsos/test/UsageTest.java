package org.boudnik.ariadne.opsos.test;

import org.boudnik.ariadne.Dimension;
import org.boudnik.ariadne.opsos.Device;
import org.boudnik.ariadne.opsos.Usage;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author Alexandre_Boudnik
 * @since 05/18/2018
 */
public class UsageTest {

    private Usage usage;
    private Device device;

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
    public void build() {
        usage.print(0);
    }
}
