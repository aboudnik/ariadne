package org.apache.ariadne;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Alexandre_Boudnik
 * @since 10/11/2018
 */
public class TimeSeries {
    private final long[] date;
    private final long[] asOf;
    private final int[] type;
    private final double[] rate;

    private TimeSeries(long[] date, long[] asOf, int[] type, double[] rate) {
        this.date = date;
        this.asOf = asOf;
        this.type = type;
        this.rate = rate;
    }

    public final static TimeSeries START = new TimeSeries(0);

    private TimeSeries(int size) {
        this(new long[size], new long[size], new int[size], new double[size]);
    }


    private TimeSeries(long date, long asOf, int type, double rate) {
        this(new long[]{date}, new long[]{asOf}, new int[]{type}, new double[]{rate});
    }

    private static int compare(final TimeSeries a, int iA, final TimeSeries b, int iB) {
        int c = Long.compare(a.date[iA], b.date[iB]);
        if (c == 0)
            c = Integer.compare(a.type[iA], b.type[iB]);
        if (c == 0)
            c = Long.compare(a.asOf[iA], b.asOf[iB]);
        return c;
    }

    public TimeSeries add(Date date, int type, double rate) {
        return merge(new TimeSeries(date.getTime(), date.getTime(), type, rate));
    }

    public TimeSeries add(Date date, Date asOf, int type, double rate) {
        return merge(new TimeSeries(date.getTime(), asOf.getTime(), type, rate));
    }

    @SuppressWarnings("WeakerAccess")
    public TimeSeries merge(TimeSeries ts) {
        TimeSeries r = new TimeSeries(this.date.length + ts.date.length);
        int iA = 0, iB = 0;
        while (iA < this.date.length && iB < ts.date.length) {
            boolean less = compare(this, iA, ts, iB) < 0;
            r.set(iA + iB, less ? this : ts, less ? iA++ : iB++);
        }
        while (iA < this.date.length)
            r.set(iA + iB, this, iA++);
        while (iB < ts.date.length)
            r.set(iA + iB, ts, iB++);
        return r;
    }

    private void set(int dst, TimeSeries ts, int src) {
        this.date[dst] = ts.date[src];
        this.asOf[dst] = ts.asOf[src];
        this.type[dst] = ts.type[src];
        this.rate[dst] = ts.rate[src];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0, length = date.length; i < length; i++) {
            sb.append(String.format("%n%-23s %-23s %d %.2f", new Timestamp(date[i]), asOf[i] == date[i] ? "" : new Timestamp(asOf[i]), type[i], rate[i]));
        }
        return sb.toString();
    }

    public TimeSeries asOf(Date asOf) {
        long asOfTime = asOf.getTime();
        int length = date.length;
        if (length == 0) {
            return START;
        }
        TimeSeries ts = START;
        int i = 0;
        do {
            int type = this.type[i];
            long date = this.date[i];
            int max = i;
            for (; i < length && type == this.type[i] && date == this.date[i]; i++) {
                if (asOfTime <= this.asOf[i]) {
                    max = i;
                }
            }
            ts = ts.merge(new TimeSeries(this.date[max], this.asOf[max], this.type[max], this.rate[max]));
        } while (i < length && asOfTime >= this.date[i]);
        return ts;
    }

    public TimeSeries asOfNow() {
        return asOf(new Date());
    }
}
