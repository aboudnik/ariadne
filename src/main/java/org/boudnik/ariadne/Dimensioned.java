package org.boudnik.ariadne;

import java.util.*;

/**
 * @author Alexandre_Boudnik
 * @since 05/18/2018
 */
public abstract class Dimensioned extends DataBlock {

    public Dimensioned(Dimension... dimensions) {
        for (Dimension dimension : dimensions) {
            this.dimensions().put(dimension.name, dimension.limit);
        }
    }

}
