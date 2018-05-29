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
public class Device extends DataBlock<Hardware.Record> {

    public Device(Dimension... dimensions) {
        super(dimensions);
    }

    @Override
    public Hardware.Record record() {
        return new Hardware.Record();
    }

    @Override
    public Set<? extends Resource> prerequisites() {
        Map<String, ?> dimensions = dimensions();
        return new HashSet<>(Arrays.asList(
                new Status(
                        new Dimension("month", dimensions.get("month")),
                        new Dimension("state", dimensions.get("state")),
                        new Dimension("city", dimensions.get("city")),
                        new Dimension("operational", dimensions.get("operational"))
                ),
                new Hardware(
                        new Dimension("state", dimensions.get("state")),
                        new Dimension("city", dimensions.get("city"))
                )
        ));
    }
}
