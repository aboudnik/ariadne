package org.boudnik.ariadne;

import java.io.Serializable;

/**
 * @author Alexandre_Boudnik
 * @since 07/11/2018
 */
@SuppressWarnings("WeakerAccess")
public interface Builder<R> extends java.util.function.Function<Node<R>, R>, Serializable {
}
