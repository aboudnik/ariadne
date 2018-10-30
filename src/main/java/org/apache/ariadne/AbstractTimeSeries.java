package org.apache.ariadne;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Alexandre_Boudnik
 * @since 10/24/2018
 */
public abstract class AbstractTimeSeries implements TimeSeries {

    @NotNull
    @Contract("-> new")
    protected abstract TimeSeries createTimeSeries();

    @NotNull
    @Contract("_ -> new")
    protected abstract TimeSeries createTimeSeries(int size);

    @NotNull
    @Contract("_, _, _, _ -> new")
    protected abstract TimeSeries createTimeSeries(long date, long asOf, int type, double rate);

    @Override
    public TimeSeries add(Date date, Date asOf, int type, double rate) {
        return merge(createTimeSeries(date.getTime(), asOf.getTime(), type, rate));
    }

    @Override
    public TimeSeries asOf(Date asOf) {
        int length = getLength();
        TimeSeries ts = createTimeSeries();
        if (length == 0) {
            return ts;
        }
        long asOfTime = asOf.getTime();
        int i = 0;
        do {
            int type = getType(i);
            long date = getDate(i);
            int max = i;
            for (; i < length && type == getType(i) && date == getDate(i); i++) {
                if (asOfTime <= getAsOf(i)) {
                    max = i;
                }
            }
            ts = ts.merge(createTimeSeries(getDate(max), getAsOf(max), getType(max), getRate(max)));
        } while (i < length && asOfTime >= getDate(i));
        return ts;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0, length = getLength(); i < length; i++) {
            sb.append(String.format("%n%-23s %-23s %d %.2f", new Timestamp(getDate(i)), getAsOf(i) == getDate(i) ? "" : new Timestamp(getAsOf(i)), getType(i), getRate(i)));
        }
        return sb.toString();
    }

    @Override
    public TimeSeries merge(@NotNull TimeSeries that) {
        int iA = 0, iB = 0, lenA = getLength(), lenB = that.getLength();
        TimeSeries r = createTimeSeries(lenA + lenB);
        while (iA < lenA && iB < lenB) {
            boolean less = compare(this, iA, that, iB) < 0;
            r.set(iA + iB, less ? this : that, less ? iA++ : iB++);
        }
        while (iA < lenA)
            r.set(iA + iB, this, iA++);
        while (iB < lenB)
            r.set(iA + iB, that, iB++);
        return r;
    }

    @Contract(pure = true)
    private static int compare(@NotNull TimeSeries a, int iA, @NotNull TimeSeries b, int iB) {
        int c = Long.compare(a.getDate(iA), b.getDate(iB));
        if (c == 0)
            c = Integer.compare(a.getType(iA), b.getType(iB));
        if (c == 0)
            c = Long.compare(a.getAsOf(iA), b.getAsOf(iB));
        return c;
    }
}
