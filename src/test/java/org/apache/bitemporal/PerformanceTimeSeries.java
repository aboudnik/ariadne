package org.apache.bitemporal;

import org.apache.bitemporal.TimeSeries;
import org.apache.bitemporal.TimeSeriesImpl;
import org.apache.ariadne.cache.TieredCache;
import org.apache.ariadne.cache.impl.GridCache;
import org.apache.ariadne.cache.impl.JavaCache;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.ehcache.sizeof.SizeOf;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;

/**
 * @author Alexandre_Boudnik
 * @since 10/09/2018
 */
public class PerformanceTimeSeries {

    private static final int EQUITIES = 60_000;
    private static final int DAYS = 252 * 15;
    private SizeOf sizeOf;

    @Test
    public void performance00() throws IOException {
        TimeSeries ts1000 = get1000days();

        final TimeSeries[] yesterday = new TimeSeries[EQUITIES];
        Date now = java.sql.Date.valueOf("2018-10-15");
        for (int i = 0; i < EQUITIES; i++) {
            yesterday[i] = ts1000.add(now, 42, 42.0);
        }

        final TimeSeries[] today = new TimeSeries[EQUITIES];
        Date tomorrow = java.sql.Date.valueOf("2018-10-16");
        for (int i = 0; i < EQUITIES; i++) {
            today[i] = yesterday[i].add(tomorrow, 42, 142.00);
            yesterday[i] = null;
        }

//        sizeOf = SizeOf.newInstance();
//        long l = sizeOf.sizeOf(ts1000);
    }

    private TimeSeries get1000days() {
        TimeSeries ts1000 = TimeSeriesImpl.START;
        long inception = java.sql.Date.valueOf("2016-10-07").getTime();
        for (int i = 0; i < DAYS; i++) {
            ts1000 = ts1000.add(new Date(inception + i * (1_000L * 24 * 3600)), 42, 133.55);
        }
        return ts1000;
    }
}