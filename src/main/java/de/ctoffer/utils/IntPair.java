package de.ctoffer.utils;

import java.util.Objects;

public class IntPair {
    public final int first;
    public final int second;

    public IntPair(final int first, final int second) {
        this.first = first;
        this.second = second;
    }

    public int getFirst() {
        return first;
    }

    public int getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return "IntPair{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }

    @Override
    public boolean equals(final Object object) {
        final boolean result;

        if (this == object) {
            result = true;
        } else if (object == null || getClass() != object.getClass()) {
            result = false;
        } else {
            IntPair other = (IntPair) object;
            result = first == other.first && second == other.second;
        }

        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
