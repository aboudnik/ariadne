package org.boudnik.ariadne.opsos;

import org.boudnik.ariadne.DataBlock;
import org.boudnik.ariadne.Dimension;

/**
 * @author Alexandre_Boudnik
 * @since 05/23/2018
 */
public class Status extends DataBlock<Status.Record> {
    public static class Record {
        int device;
        boolean operational;
    }

    public Status(Dimension... dimensions) {
        super(dimensions);
    }

    @Override
    protected Record record() {
        return new Record();
    }

}
