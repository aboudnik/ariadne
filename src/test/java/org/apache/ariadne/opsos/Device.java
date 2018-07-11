package org.apache.ariadne.opsos;

import org.apache.spark.sql.Row;
import org.apache.ariadne.DataBlock;
import org.apache.ariadne.Dimension;
import org.apache.ariadne.Resource;

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
    public Hardware.Record valueOf(Row row) {
        return null;
    }

    @Override
    public String sql() {
        return null;
    }

    @Override
    public Set<Resource> prerequisites() {
        Map<String, ?> dimensions = dimensions();
        return dimensions(
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
        );
    }
}
