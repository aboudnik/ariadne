package org.boudnik.ariadne.resourceinventory;

import java.util.Arrays;

/**
 * @author Sergey Nuyanzin
 * @since 5/8/2018
 */
public class RIReportOutputColumns {
    private String[] column_names;

    public String[] getColumn_names() {
        return column_names;
    }

    public RIReportOutputColumns setColumn_names(String[] column_names) {
        this.column_names = column_names;
        return this;
    }

    @Override
    public String toString() {
        return "RIReportOutputColumns{" +
                "column_names=" + Arrays.toString(column_names) +
                '}';
    }
}
