package de.ctoffer.commons.test.util;

import org.junit.jupiter.api.Assertions;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

public class TestStep {
    public static final Condition IS_TRUE = Assertions::assertTrue;
    public static final Condition IS_FALSE = Assertions::assertFalse;

    public static <T> Comparison<T> IsNull() {
        return Assertions::assertNull;
    }

    public static <T> Comparison<T> IsNotNull() {
        return Assertions::assertNotNull;
    }

    public static <T> Comparison<T> Equals(T expected) {
        return actual -> assertEquals(expected, actual);
    }

    public static <T> Comparison<T[]> Equals(T[] other) {
        return object -> assertArrayEquals(other, object);
    }

    public static Comparison<int[]> Equals(int[] other) {
        return object -> assertArrayEquals(other, object);
    }

    public static <T> Comparison<T> EqualsNot(T unexpected) {
        return actual -> assertNotEquals(unexpected, actual);
    }

    public static <T> Comparison<T> HasSameReferenceAs(T expected) {
        return actual -> assertSame(expected, actual);
    }

    public static <T> Comparison<T> HasDifferentReferenceAs(T unexpected) {
        return actual -> assertNotSame(unexpected, actual);
    }

    public static <T> Comparison<T> HasSameHashAs(T expected) {
        return actual -> assertEquals(expected.hashCode(), actual.hashCode());
    }

    public static <T> Comparison<T> HasDifferentHashAs(T unexpected) {
        return actual -> assertNotEquals(unexpected.hashCode(), actual.hashCode());
    }

    public static <T> Comparison<T> Fulfills(Predicate<T> condition) {
        return condition::test;
    }

    public static <T> Comparison<T> FulfillsNot(Predicate<T> condition) {
        return condition.negate()::test;
    }

    public static <T, X> Comparison<T> Where(Function<T, X> extraction, Comparison<X> condition) {
        return object -> condition.withObject(extraction.apply(object));
    }

    public static Comparison<Integer> LessThan(Integer other) {
        return object -> assertTrue(object < other);
    }

    public static Comparison<Integer> GreaterThan(Integer other) {
        return object -> assertTrue(object > other);
    }

    public static <X> EntryComparison<X> EqualsEntryOf(X[] other) {
        return EqualsEntryOf(Arrays.asList(other));
    }

    public static <X> EntryComparison<X> EqualsEntryOf(Iterable<X> other) {
        return new EntryComparison<X>() {
            private Iterator<X> iterator = other.iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public void withNextObject(X object) {
                Equals(iterator.next()).withObject(object);
            }
        };
    }

    public static <T extends Iterable<X>, X> Comparison<T> WhereEachEntry(EntryComparison<X> condition) {
        return iterable -> {
            for (X element : iterable) {
                Assertions.assertTrue(condition.hasNext(), "Iterable in condition has less elements than given Iterable.");
                condition.withNextObject(element);
            }
            Assertions.assertFalse(condition.hasNext(), "Iterable in condition has more elements than given Iterable.");
        };
    }
}
