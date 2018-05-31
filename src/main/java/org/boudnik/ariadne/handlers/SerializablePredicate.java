package org.boudnik.ariadne.handlers;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author Sergey Nuyanzin
 * @since 5/31/2018
 */
@FunctionalInterface
public interface SerializablePredicate<T> extends Predicate<T> {

    SerializablePredicate TRUE_PREDICATE = t -> true;

    default SerializablePredicate<T> and(SerializablePredicate<? super T> other) {
        Objects.requireNonNull(other);
        return (t) -> test(t) && other.test(t);
    }
}
