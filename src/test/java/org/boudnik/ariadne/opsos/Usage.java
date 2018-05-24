package org.boudnik.ariadne.opsos;

import org.boudnik.ariadne.DataBlock;
import org.boudnik.ariadne.Dimension;
import org.boudnik.ariadne.DG;
import org.boudnik.ariadne.Resource;

import java.util.*;
import java.util.function.Supplier;

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
    }

    @Override
    protected Record record() {
        return new Record();
    }

    public Usage(Dimension... dims) {
        super(dims);
    }

    @Override
    public Set<? extends Resource> prerequisites() {
        Map<String, ?> dimensions = dimensions();
        return new HashSet<>(Arrays.asList(
                new Traffic(
                        DG.generate(dimensions,
                                new HashMap<String, Supplier>() {{
                                    put("month", () -> "month");
                                    put("state", () -> "state");
                                }})
                ),
                new Device(
                        DG.generate(dimensions,
                                new HashMap<String, Supplier>() {{
                                    put("state", () -> "state");
                                    put("city", () -> "city");
                                    put("month", () -> "month");
                                    put("operational", () -> false);
                                }})
                ).as("offline"),
                new Device(
                        DG.generate(dimensions,
                                new HashMap<String, Supplier>() {{
                                    put("state", () -> "state");
                                    put("city", () -> "city");
                                    put("month", () -> "month");
                                    put("operational", () -> true);
                                }})
                ).as("online")
        ));
    }

}
