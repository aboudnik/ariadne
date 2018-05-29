package org.boudnik.ariadne.handlers;

import org.boudnik.ariadne.DataBlock;

import java.io.IOException;
import java.util.Collection;

/**
 * @author Sergey Nuyanzin
 * @since 5/28/2018
 */
public interface Handler<R> {
    Collection<R> handle(DataBlock<R> dataBlock) throws IOException, IllegalAccessException;
}
