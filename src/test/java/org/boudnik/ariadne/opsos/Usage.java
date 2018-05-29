package org.boudnik.ariadne.opsos;

import org.boudnik.ariadne.DataBlock;
import org.boudnik.ariadne.Dimension;
import org.boudnik.ariadne.Resource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Alexandre_Boudnik
 * @since 05/18/2018
 */
public class Usage extends DataBlock<Usage.Record> {

    public static class Record {
        String state;
        String city;
        int device;
        String month;
        double gigabytes;

        public Record(String city, String state, String month) {
            this.state = state;
            this.city = city;
            this.month = month;
        }

        public Record() {

        }
    }

    @Override
    public Record record() {
        return new Record();
    }

    public Record record(String city, String state, String month) {
        return new Record(city, state, month);
    }

    public Usage(Dimension... dims) {
        super(dims);
    }

    @Override
    public Set<? extends Resource> prerequisites() {
        Map<String, ?> dimensions = dimensions();
        return new HashSet<>(Arrays.asList(
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
        ));
    }

}
