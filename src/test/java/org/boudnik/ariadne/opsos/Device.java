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
public class Device extends DataBlock<Device.Record> {
    public static class Record {
        int device;
        String state;
        String city;
    }

    @Override
    protected Record record() {
        return new Record();
    }

    public Device(Dimension... dimensions) {
        super(dimensions);
    }

    @Override
    public Set<? extends Resource> prerequisites() {
        Map<String, ?> dimensions = dimensions();
        return new HashSet<>(Arrays.asList(
                new Status(new Dimension("month", dimensions.get("month"))))
        );
    }

    @Override
    public boolean isReady() {
        return false;
    }
}
