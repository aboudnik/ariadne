package org.boudnik.ariadne.opsos;

import org.boudnik.ariadne.Dimension;
import org.boudnik.ariadne.External;

import java.util.Date;

/**
 * @author Alexandre_Boudnik
 * @since 05/18/2018
 */
public class Traffic extends External<Traffic.Record> {
    public static class Record {
        Date month;
        int device;
        double gigabytes;

        @Override
        public String toString() {
            return "Record{" +
                    "month=" + month +
                    ", device=" + device +
                    ", gigabytes=" + gigabytes +
                    '}';
        }
    }

    @Override
    public Record record() {
        return new Record();
    }

    public Traffic(Dimension... dims) {
        super(dims);
    }
}
