package de.ctoffer.commons.container;

import de.ctoffer.commons.container.primitive.CharIterator;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.IntFunction;

public enum ArrayUtils {
    ;


    public static void checkIfArray(final Object maybeArray) {
        if (isNotArray(maybeArray)) {
            throw new IllegalArgumentException("Argument must be array.");
        }
    }

    public static boolean isNotArray(final Object obj) {
        return !isArray(obj);
    }

    public static boolean isArray(final Object obj) {
        return obj.getClass().isArray();
    }

    public static Class<?> getComponentType(final Object object) {
        return object.getClass().getComponentType();
    }

    public static <T> Class<T> getComponentType(final T[] array) {
        return (Class<T>) array.getClass().getComponentType();
    }

    public static <T> T createEmptyArrayFrom(final Object object) {
        return createArray(getComponentType(object), 0);
    }

    public static <T> T[] createEmptyArrayFrom(final T[] array) {
        return createEmptyArray(getComponentType(array));
    }

    public static <T> T createEmptyArray(final Class<?> cls) {
        return createArray(cls, 0);
    }

    public static <T> IntFunction<T[]> arrayGenerator(final T[] array) {
        return size -> createArray(getComponentType(array), size);
    }

    public static <T> T createArray(final Class<?> cls, int length) {
        return (T) Array.newInstance(cls, length);
    }

    public static <E> Iterable<E> toIterable(E[] array) {
        return () -> iterator(array);
    }

    public static <E> Iterator<E> iterator(
            final E[] array
    ) {
        return iterator(array, 0);
    }

    public static <E> Iterator<E> iterator(
            final E[] array,
            final int initialPosition
    ) {
        return new Iterator<>() {
            private int position = initialPosition;

            @Override
            public boolean hasNext() {
                return position < array.length;
            }

            @Override
            public E next() {
                if (position >= array.length) {
                    throw new NoSuchElementException("Array of length " + array.length + " has no entry at position " + position);
                }
                return array[position++];
            }
        };
    }


    public static CharIterator iterator(
            final char[] array
    ) {
        return new CharIterator() {
            private int position = 0;

            @Override
            public boolean hasNext() {
                return position < array.length;
            }

            @Override
            public char next() {
                if (position >= array.length) {
                    throw new NoSuchElementException("Array of length " + array.length + " has no entry at position " + position);
                }
                return array[position++];
            }

            @Override
            public Iterator<Character> boxed() {
                final Character[] characters = new Character[array.length];
                for (int i = 0; i < array.length; ++i) {
                    characters[i] = array[i];
                }
                return iterator(characters, position);
            }
        };
    }

    @SuppressWarnings("SuspiciousSystemArraycopy")
    public static <T> T copy(final Object array, int start, int stop) {
        checkIfArray(array);

        int size = Array.getLength(array);
        if (stop > size) {
            stop = size;
        }

        int newLength = stop - start;
        Class<?> componentType = getComponentType(array);
        final Object resultArray = ArrayUtils.createArray(componentType, newLength);
        System.arraycopy(array, start, resultArray, 0, newLength);
        return (T) resultArray;
    }
}
