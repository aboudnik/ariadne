package org.apache.ariadne.opsos;

import org.apache.spark.sql.Row;
import org.apache.ariadne.Dimension;
import org.apache.ariadne.External;

import java.io.Serializable;

/**
 * @author Alexandre_Boudnik
 * @since 05/18/2018
 */
public class Hardware extends External<Hardware.Record> {
    @Override
    public Record valueOf(Row row) {
        Record hardware = new Record();
        Object device = row.get(0);
        hardware.setDevice(device instanceof Integer? (int) device : Integer.valueOf((String)device));
        hardware.setState(row.getString(1));
        hardware.setCity(row.getString(2));
        return hardware;
    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    public static class Record implements Serializable {
        private int device;
        private String state;
        private String city;

        public int getDevice() {
            return device;
        }

        public void setDevice(int device) {
            this.device = device;
        }

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

        @Override
        public String toString() {
            return "Record{" +
                    "device=" + device +
                    ", state='" + state + '\'' +
                    ", city='" + city + '\'' +
                    '}';
        }
    }

    @Override
    public Record record() {
        return new Record();
    }

    public Hardware(Dimension... dimensions) {
        super(dimensions);
    }

}
