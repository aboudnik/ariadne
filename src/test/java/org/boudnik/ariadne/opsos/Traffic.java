package org.boudnik.ariadne.opsos;

import org.apache.spark.sql.Row;
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
        public Integer device;
        public Integer port;
        public Double gigabytes;

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

        public Integer getDevice() {
            return device;
        }

        public void setDevice(Integer device) {
            this.device = device;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public Double getGigabytes() {
            return gigabytes;
        }

        public void setGigabytes(Double gigabytes) {
            this.gigabytes = gigabytes;
        }
    }

    @Override
    public Record record() {
        return new Record();
    }

    @Override
    public Record valueOf(Row row) {
        Traffic.Record traffic = new Traffic.Record();
        traffic.setMonth((Date) dimensions().get("month"));
        traffic.setGigabytes(Double.valueOf(row.getString(2)));
        traffic.setPort(Integer.valueOf(row.getString(1)));
        traffic.setDevice(Integer.valueOf(row.getString(0)));
        return traffic;
    }
}
