package org.apache.ariadne;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Alexandre_Boudnik
 * @since 10/24/2018
 */
public interface TimeSeries extends Serializable {

    @Contract(pure = true)
    default int compare(int iA, @NotNull TimeSeries that, int iB) {
        int c = Long.compare(getDate(iA), that.getDate(iB));
        if (c == 0)
            c = Integer.compare(getType(iA), that.getType(iB));
        if (c == 0)
            c = Long.compare(getAsOf(iA), that.getAsOf(iB));
        return c;
    }

    @NotNull
    @Contract("_ -> new")
    TimeSeries createTimeSeries(int size);

    @Contract(pure = true)
    long getDate(int i);

    @Contract(pure = true)
    int getType(int i);

    @Contract(pure = true)
    long getAsOf(int i);

    @Contract(pure = true)
    double getRate(int i);

    @Contract(pure = true)
    int getLength();

    default TimeSeries add(Date date, int type, double rate) {
        return add(date, date, type, rate);
    }

    TimeSeries add(Date date, Date asOf, int type, double rate);

    default TimeSeries merge(TimeSeries that) {
        int iA = 0, iB = 0, lenA = getLength(), lenB = that.getLength();
        TimeSeries r = createTimeSeries(lenA + lenB);
        while (iA < lenA && iB < lenB) {
            boolean less = this.compare(iA, that, iB) < 0;
            r.set(iA + iB, less ? this : that, less ? iA++ : iB++);
        }
        while (iA < lenA)
            r.set(iA + iB, this, iA++);
        while (iB < lenB)
            r.set(iA + iB, that, iB++);
        return r;
    }

    void set(int dst, @NotNull TimeSeries that, int src);

    default String presentation() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0, length = getLength(); i < length; i++) {
            sb.append(String.format("%n%-23s %-23s %d %.2f", new Timestamp(getDate(i)), getAsOf(i) == getDate(i) ? "" : new Timestamp(getAsOf(i)), getType(i), getRate(i)));
        }
        return sb.toString();
    }

    abstract TimeSeries asOf(Date asOf);

    default TimeSeries asOfNow() {
        return asOf(new Date());
    }
}
