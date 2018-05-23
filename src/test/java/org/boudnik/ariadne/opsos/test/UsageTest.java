package org.boudnik.ariadne.opsos.test;

import org.boudnik.ariadne.Dimension;
import org.boudnik.ariadne.opsos.Device;
import org.boudnik.ariadne.opsos.Usage;
import org.junit.Test;

/**
 * @author Alexandre_Boudnik
 * @since 05/18/2018
 */
public class UsageTest {
    @Test
    public void report() {
        Usage usage = new Usage(
                new Dimension("month", "2018-01-01"),
                new Dimension("state", "VA"),
                new Dimension("city", "Leesburg")
        );
        System.out.println("usage = " + usage);
        System.out.println("usage.prerequisites() = " + usage.prerequisites());

        Device device = new Device(new Dimension("month", "2018-01-01"));
        System.out.println("device.prerequisites() = " + device.prerequisites());
    }
}
