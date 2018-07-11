package org.apache.ariadne;

import java.io.Serializable;

/**
 * @author Alexandre_Boudnik
 * @since 07/11/2018
 */
public interface Builder<R> extends java.util.function.Function<Node<R>, R>, Serializable {
}
