package de.ctoffer.commons.container;

import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
public class SerializablePair<F extends Serializable, S extends Serializable>
        extends Pair<F, S>
        implements Serializable {

    public static <A extends Serializable, B extends Serializable> SerializablePair<A, B> of(final Pair<A, B> pair) {
        SerializablePair<A, B> result;

        if (pair instanceof SerializablePair) {
            result = (SerializablePair<A, B>) pair;
        } else {
            Objects.requireNonNull(
                    pair,
                    "Pair must be non-null"
            );
            result = SerializablePair.of(
                    pair.first,
                    pair.second
            );
        }

        return result;
    }

    public static <A extends Serializable, B extends Serializable> SerializablePair<A, B> of(
            final A first,
            final B second
    ) {
        return new SerializablePair<>(
                first,
                second
        );
    }

    public SerializablePair(
            final F first,
            final S second
    ) {
        super(first, second);
    }


}
