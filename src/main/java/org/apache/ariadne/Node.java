package org.apache.ariadne;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author Alexandre_Boudnik
 * @since 07/11/2018
 */
public interface Node<P> extends Serializable {
    Collection<Node<P>> getChildren();

    P payload();
}
