package org.boudnik.ariadne.opsos;

import org.boudnik.ariadne.Dimension;
import org.boudnik.ariadne.External;

/**
 * @author Alexandre_Boudnik
 * @since 05/23/2018
 */
public class Status extends External<Status.Record> {
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
