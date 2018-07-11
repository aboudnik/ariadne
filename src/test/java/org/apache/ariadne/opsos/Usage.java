package org.apache.ariadne.opsos;

import org.apache.spark.sql.Row;
import org.apache.ariadne.DataBlock;
import org.apache.ariadne.Dimension;
import org.apache.ariadne.Resource;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * @author Alexandre_Boudnik
 * @since 05/18/2018
 */
public class Usage extends DataBlock<Usage.Record> {

    @SuppressWarnings({"unused", "WeakerAccess"})
    public static class Record implements Serializable {
        private String state;
        private String city;
        private int device;
        private String month;
        private double gigabytes;

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public int getDevice() {
            return device;
        }

        public void setDevice(int device) {
            this.device = device;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public double getGigabytes() {
            return gigabytes;
        }

        public void setGigabytes(double gigabytes) {
            this.gigabytes = gigabytes;
        }
    }

    @Override
    public String sql() {
        return null;
    }

    @Override
    public Record record() {
        return new Record();
    }

    @Override
    public Record valueOf(Row row) {
        return null;
    }

    public Usage(Dimension... dims) {
        super(dims);
    }

    @Override
    public Set<Resource> prerequisites() {
        Map<String, ?> dimensions = dimensions();
        return dimensions(
                new Traffic(
                        new Dimension("month", dimensions.get("month")),
                        new Dimension("state", dimensions.get("state"))
                ),
                new Device(
                        new Dimension("state", dimensions.get("state")),
                        new Dimension("city", dimensions.get("city")),
                        new Dimension("month", dimensions.get("month")),
                        new Dimension("operational", false)
                ).as("offline"),
                new Device(
                        new Dimension("state", dimensions.get("state")),
                        new Dimension("city", dimensions.get("city")),
                        new Dimension("month", dimensions.get("month")),
                        new Dimension("operational", true)
                ).as("online")
        );
    }

}
