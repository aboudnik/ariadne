package org.boudnik.ariadne.opsos;

import org.boudnik.ariadne.DataBlock;
import org.boudnik.ariadne.Dimension;
import org.boudnik.ariadne.Resource;

import java.util.Date;
import java.util.Set;

/**
 * @author Alexandre_Boudnik
 * @since 05/18/2018
 */
public class Traffic extends DataBlock<Traffic.Record> {
    public static class Record {
        Date month;
        int device;
        double gigabytes;
    }

    @Override
    protected Record record() {
        return new Record();
    }

    public Traffic(Dimension... dims) {
        super(dims);
    }

    @Override
    public boolean isReady() {
        return false;
    }
}
