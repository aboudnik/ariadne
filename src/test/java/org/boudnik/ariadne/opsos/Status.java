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
 * @since 05/23/2018
 */
public class Status extends DataBlock<Status.Record> {
    public static class Record {

    }

    public Status(Dimension... dimensions) {
        super(dimensions);
    }

    @Override
    protected Record record() {
        return new Record();
    }

    @Override
    public boolean isReady() {
        return true;
    }
}
