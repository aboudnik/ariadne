package org.apache.bitemporal;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Alexandre_Boudnik
 * @since 10/24/2018
 */
public interface TimeSeries extends Serializable {

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

    void set(int dst, @NotNull TimeSeries that, int src);

    TimeSeries add(Date date, Date asOf, int type, double rate);

    default TimeSeries add(Date date, int type, double rate) {
        return add(date, date, type, rate);
    }

    TimeSeries merge(@NotNull TimeSeries that);

    TimeSeries asOf(Date asOf);

    default TimeSeries asOfNow() {
        return asOf(new Date());
    }
}
