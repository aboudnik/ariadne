package org.apache.ariadne.opsos;

import org.apache.spark.sql.Row;
import org.apache.ariadne.Dimension;
import org.apache.ariadne.External;

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

    @SuppressWarnings({"unused", "WeakerAccess"})
    public static class Record implements Serializable {
        private int device;
        private boolean operational;

        public int getDevice() {
            return device;
        }

        public void setDevice(int device) {
            this.device = device;
        }

        public boolean isOperational() {
            return operational;
        }

        public void setOperational(boolean operational) {
            this.operational = operational;
        }
    }

    public Status(Dimension... dimensions) {
        super(dimensions);
    }

    @Override
    public Record record() {
        return new Record();
    }

}
