package org.boudnik.ariadne.handlers;

import org.boudnik.ariadne.DataBlock;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Sergey Nuyanzin
 * @since 5/28/2018
 */
public class JdbcHandler implements Handler {
    @Override
    public Collection handle(DataBlock dataBlock) {
        return Collections.emptyList();
    }
}
