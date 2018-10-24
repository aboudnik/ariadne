package org.apache.ariadne;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

/**
 * @author Alexandre_Boudnik
 * @since 10/11/2018
 */
public class TimeSeriesImpl extends AbstractTimeSeries {
    public static final TimeSeries START = new TimeSeriesImpl(0);
    private final long[] date;
    private final long[] asOf;
    private final int[] type;
    private final double[] rate;

    private TimeSeriesImpl(long[] date, long[] asOf, int[] type, double[] rate) {
        this.date = date;
        this.asOf = asOf;
        this.type = type;
        this.rate = rate;
    }

    private TimeSeriesImpl(int size) {
        this(new long[size], new long[size], new int[size], new double[size]);
    }


    private TimeSeriesImpl(long date, long asOf, int type, double rate) {
        this(new long[]{date}, new long[]{asOf}, new int[]{type}, new double[]{rate});
    }

    @Override
    @NotNull
    @Contract("_, _, _, _ -> new")
    protected TimeSeries createTimeSeries(long date, long asOf, int type, double rate) {
        return new TimeSeriesImpl(date, asOf, type, rate);
    }

    @Override
    @NotNull
    @Contract("_ -> new")
    public TimeSeries createTimeSeries(int size) {
        return new TimeSeriesImpl(size);
    }

    @Override
    @Contract(pure = true)
    public long getDate(int i) {
        return date[i];
    }

    @Override
    @Contract(pure = true)
    public int getType(int i) {
        return type[i];
    }

    @Override
    @Contract(pure = true)
    public long getAsOf(int i) {
        return asOf[i];
    }

    @Override
    @Contract(pure = true)
    public double getRate(int i) {
        return rate[i];
    }

    @Override
    @Contract(pure = true)
    public int getLength() {
        return date.length;
    }

    @Override
    public void set(int dst, @NotNull TimeSeries that, int src) {
        this.date[dst] = that.getDate(src);
        this.asOf[dst] = that.getAsOf(src);
        this.type[dst] = that.getType(src);
        this.rate[dst] = that.getRate(src);
    }

    @Override
    public TimeSeries getStart() {
        return START;
    }
}