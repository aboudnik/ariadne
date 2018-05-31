package org.boudnik.ariadne.opsos;

import org.apache.spark.sql.Row;
import org.boudnik.ariadne.Dimension;
import org.boudnik.ariadne.External;

import java.io.Serializable;

/**
 * @author Alexandre_Boudnik
 * @since 05/23/2018
 */
public class Status extends External<Status.Record> {
    @Override
    public Record valueOf(Row row) {
        return null;
    }

    public static class Record implements Serializable {
        int device;
        boolean operational;
    }

    public Status(Dimension... dimensions) {
        super(dimensions);
    }

    @Override
    public Record record() {
        return new Record();
    }

}
