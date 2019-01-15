package org.apache.bitemporal;

import org.apache.bitemporal.TimeSeries;
import org.apache.bitemporal.TimeSeriesImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

/**
 * @author Alexandre_Boudnik
 * @since 10/09/2018
 */
public class TimeSeriesTest {

    private Date oct07;
    private Date oct08;
    private Date oct09;

    @Before
    public void setUp() {
        oct07 = java.sql.Date.valueOf("2018-10-07");
        oct08 = java.sql.Date.valueOf("2018-10-08");
        oct09 = java.sql.Date.valueOf("2018-10-09");
    }

    @Test
    public void test1() {
        TimeSeries ts = TimeSeriesImpl.START;
        ts = ts.add(oct07, 42, 133.55);
        ts = ts.add(oct07, oct08, 42, 131.11);
        ts = ts.add(oct07, 11, 133.55);

        ts = ts.add(oct07, oct09, 42, 131.11);

        ts = ts.add(oct07, oct08, 11, 131.11);
        ts = ts.add(oct07, oct09, 11, 131.11);

        ts = ts.add(oct09, 42, 129.99);

        ts = ts.add(oct08, oct08, 42, 130.00);
        ts = ts.add(oct08, new Date(oct08.getTime() + 10_000), 42, 130.10);
        ts = ts.add(oct08, new Date(oct08.getTime() + 5_000), 42, 130.05);

        System.out.println("ts All = " + ts);

        System.out.println("ts.asOf(oct08) = " + ts.asOf(oct08));
        System.out.println("ts.asOf(oct09) = " + ts.asOf(oct09));
        System.out.println("ts.asOfNow() = " + ts.asOfNow());
    }
}