package org.apache.ariadne.opsos;


import org.apache.spark.sql.Row;
import org.apache.ariadne.DataBlock;
import org.apache.ariadne.Dimension;
import org.apache.ariadne.Resource;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Alexandre_Boudnik
 * @since 05/30/2018
 */
public class Total extends DataBlock<Total.Record> {

    public Total(Dimension... dimensions) {
        super(dimensions);
    }

    @Override
    public Record record() {
        return new Record();
    }

    @Override
    public Record valueOf(Row row) {
        Record record = new Record();
        record.setTotal(row.getDouble(0));
        return record;
    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    public static class Record implements Serializable {
        private Double total;
        public Double getTotal() {
            return total;
        }
        public void setTotal(Double total) {
            this.total = total;
        }
    }

    @Override
    public String sql() {
        return "select sum(gigabytes) from " + Traffic.class.getTypeName().replace(".", "_");
    }

    @Override
    public Set<Resource> prerequisites() {
        Map<String, ?> dimensions = dimensions();
        return dimensions(
                new Traffic(
                        new Dimension("month", dimensions.get("month")),
                        new Dimension("state", dimensions.get("state"))
                )
        );
    }
}
