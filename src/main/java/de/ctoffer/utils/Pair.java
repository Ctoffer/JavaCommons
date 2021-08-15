package de.ctoffer.utils;

import java.util.Objects;

public class Pair<F, S> {
    public final F first;
    public final S second;

    public Pair(final F first, final S second) {
        this.first = first;
        this.second = second;
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }

    @Override
    public boolean equals(final Object object) {
        final boolean result;

        if (this == object) {
            result = true;
        } else if (object == null || getClass() != object.getClass()) {
            result = false;
        } else {
            Pair<?, ?> other = (Pair<?, ?>) object;
            result = Objects.equals(first, other.first) && Objects.equals(second, other.second);
        }

        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
