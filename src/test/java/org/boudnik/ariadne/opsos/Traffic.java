package org.boudnik.ariadne.opsos;

import org.boudnik.ariadne.Dimension;
import org.boudnik.ariadne.External;

import java.io.Serializable;
import java.sql.Date;
import java.util.function.Function;

/**
 * @author Alexandre_Boudnik
 * @since 05/18/2018
 */
public class Traffic extends External<Traffic.Record> {
    public Traffic(Dimension... dims) {
        super(dims);
    }

    public static class Record implements Serializable {
        public Date month;
        public int device;
        public int port;
        public double gigabytes;

        @Override
        public String toString() {
            return "Record{" +
                    "month=" + month +
                    ", device=" + device +
                    ", gigabytes=" + gigabytes +
                    '}';
        }

        public Date getMonth() {
            return month;
        }

        public void setMonth(Date month) {
            this.month = month;
        }

        public int getDevice() {
            return device;
        }

        public void setDevice(int device) {
            this.device = device;
        }

        public double getGigabytes() {
            return gigabytes;
        }

        public void setGigabytes(double gigabytes) {
            this.gigabytes = gigabytes;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

    }

    @Override
    public Record record() {
        return new Record();
    }

    @Override
    public Record valueOf(String line) {
        String[] parts = line.split(",");
        Traffic.Record traffic = new Traffic.Record();
        traffic.setMonth((Date) dimensions().get("month"));
        traffic.setGigabytes(Double.valueOf(parts[2]));
        traffic.setPort(Integer.valueOf(parts[1]));
        traffic.setDevice(Integer.valueOf(parts[0]));
        return traffic;
    }
}
