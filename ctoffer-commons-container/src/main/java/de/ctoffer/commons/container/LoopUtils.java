package de.ctoffer.commons.container;

import de.ctoffer.commons.functional.ThrowingConsumer;
import de.ctoffer.commons.functional.ThrowingSupplier;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public enum LoopUtils {
    ;

    public static IntStream range(int start) {
        return range(0, start);
    }

    public static IntStream range(int start, int stop) {
        return range(start, stop, 1);
    }

    public static IntStream range(int start, int stop, int step) {
        if(step == 0) {
            throw new IllegalArgumentException("'step' must not be zero!");
        }

        IntStream result;
        if(isEmptyRange(start, stop, step)) {
            result = IntStream.range(0, 0);
        } else {
            result = IntStream.range(0, sizeOfRange(start, stop, step))
                    .map(i -> start + i * step);
        }

        return result;
    }

    public static int sizeOfRange(int start, int stop, int step) {
        return (int) Math.ceil(Math.abs(start - stop) / Math.abs((double) step));
    }

    public static boolean isEmptyRange(int start, int stop, int step) {
        boolean emptySlicePositiveStep = step > 0 && stop <= start;
        boolean emptySliceNegativeStep = step < 0 && start <= stop;
        return emptySlicePositiveStep || emptySliceNegativeStep;
    }

    public static <T> void loopUntilNull(final Supplier<T> source, final Consumer<T> action) {
        T object = source.get();
        while(object != null){
            action.accept(object);
            object = source.get();
        }
    }

    public static <T, E extends Exception, F extends Exception>
    void tryLoopUntilNull(final ThrowingSupplier<T, E> source, final ThrowingConsumer<T, F> action) throws E, F {
        T object = source.get();
        while(object != null){
            action.accept(object);
            object = source.get();
        }
    }
}