package org.boudnik.ariadne.resourceinventory;

import org.boudnik.ariadne.Resource;

import java.util.Collections;
import java.util.Set;

/**
 * @author Sergey Nuyanzin
 * @since 5/8/2018
 */
public class RIResource implements Resource {
    private LocationCondition location;
    private RIReportOutputColumns outputColumns;

    public LocationCondition getLocation() {
        return location;
    }

    public RIResource setLocation(LocationCondition location) {
        this.location = location;
        return this;
    }

    public RIReportOutputColumns getOutputColumns() {
        return outputColumns;
    }

    public RIResource setOutputColumns(RIReportOutputColumns outputColumns) {
        this.outputColumns = outputColumns;
        return this;
    }

    @Override
    public String type() {
        return "RIResource";
    }

    @Override
    public Set<Resource> prerequisites() {
        return Collections.singleton(this);
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public String toString() {
        return "RIResource{" +
                "location=" + location +
                ", outputColumns=" + outputColumns +
                '}';
    }
}
