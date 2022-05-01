package de.ctoffer.commons.container;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.IntFunction;
import java.util.function.IntSupplier;

import static de.ctoffer.commons.container.AccessUtils.limitValue;
import static de.ctoffer.commons.container.AccessUtils.normalizeIndex;
import static de.ctoffer.commons.container.ArrayUtils.*;

public enum AccessUtils {
    ;

    public static final int DEFAULT_STEP = 1;

    // ================================================================================================================
    // Get
    // ================================================================================================================


    public static <T> T get(final List<T> list, final int index) {
        return abstractGet(list.getClass().getSimpleName(), list::get, list::size, index);
    }

    private static <T> T abstractGet(final String cls,
                                     final IntFunction<T> access,
                                     final IntSupplier sizeGetter,
                                     final int index) {
        int size = sizeGetter.getAsInt();
        checkIfIndexIsInBounds(cls, size, index);

        return access.apply(normalizeIndex(index, size));
    }

    private static void checkIfIndexIsInBounds(final String cls, int size, int index) {
        if (size <= index || size < Math.abs(index)) {
            throw createIOOBEForAbstractGet(cls, size, index);
        }
    }

    private static IndexOutOfBoundsException createIOOBEForAbstractGet(final String cls, int size, int index) {
        return new IndexOutOfBoundsException(createIOOBEMessageForAbstractGet(cls, size, index));
    }

    private static String createIOOBEMessageForAbstractGet(final String cls, int size, int index) {
        return String.format(
                "Index %s is out of bounds for %s of size %s"
                , index
                , cls
                , size);
    }

    static int normalizeIndex(int index, int size) {
        int normalizedIndex = index;
        if (normalizedIndex < 0) {
            normalizedIndex += size;
        }
        return normalizedIndex;
    }

    public static char get(final CharSequence sequence, final int index) {
        return abstractGet(sequence.getClass().getSimpleName(), sequence::charAt, sequence::length, index);
    }

    public static <T> T get(final T[] array, final int index) {
        return abstractGet(
                array.getClass().getSimpleName()
                , i -> array[i]
                , () -> array.length
                , index);
    }

    public static int get(final int[] array, final int index) {
        return abstractGet(
                array.getClass().getSimpleName()
                , i -> array[i]
                , () -> array.length
                , index);
    }

    // ================================================================================================================
    // Size
    // ================================================================================================================


    public static int size(final Object object) {
        if(ArrayUtils.isArray(object)) {
            return Array.getLength(object);
        } else if(object instanceof Collection) {
            return size((Collection<?>) object);
        } else if(object instanceof Map) {
            return size((Map<?, ?>) object);
        } else if(object instanceof CharSequence) {
            return size((CharSequence) object);
        } else {
            throw new IllegalArgumentException("Object of class " + object.getClass() + " has no size.");
        }
    }

    public static <T> int size(final Collection<T> collection) {
        return collection.size();
    }

    public static <K, V> int size(final Map<K, V> map) {
        return map.size();
    }

    public static int size(CharSequence sequence) {
        return sequence.length();
    }

    public static <T> int size(T[] array) {
        return array.length;
    }

    public static int size(boolean[] array) {
        return array.length;
    }

    public static int size(byte[] array) {
        return array.length;
    }

    public static int size(short[] array) {
        return array.length;
    }

    public static int size(int[] array) {
        return array.length;
    }

    public static int size(long[] array) {
        return array.length;
    }

    public static int size(float[] array) {
        return array.length;
    }

    public static int size(double[] array) {
        return array.length;
    }

    public static int size(char[] array) {
        return array.length;
    }

    // ================================================================================================================
    // Slice: List
    // ================================================================================================================

    public static <E> List<E> slice(final List<E> list, int start) {
        return null;
    }

    public static <E> List<E> slice(final List<E> list, int start, int end) {
        return null;
    }

    public static <E> List<E> slice(final List<E> list, int start, int end, int step) {
        return null;
    }

    public static <E> List<E> slice(final List<E> list, Integer start) {
        return null;
    }

    public static <E> List<E> slice(final List<E> list, Integer start, Integer end) {
        return null;
    }

    public static <E> List<E> slice(final List<E> list, Integer start, Integer end, Integer step) {
        return null;
    }

    // ================================================================================================================
    // Slice: String
    // ================================================================================================================

    public static String slice(final String input, int start) {
        return slice(input, start, size(input));
    }

    public static String slice(final String input, int start, int end) {
        return slice(input, start, end, DEFAULT_STEP);
    }

    public static String slice(final String input, int start, int end, int step) {
        return new String((char[]) arraySlice(input.toCharArray(), start, end, step));
    }

    public static String slice(final String input, Integer start) {
        return slice(input, start, null);
    }

    public static String slice(final String input, Integer start, Integer stop) {
        return slice(input, start, stop, null);
    }

    public static String slice(final String input, Integer start, Integer stop, Integer step) {
        return arraySlice(input, start, stop, step);
    }

    // ================================================================================================================
    // Slice: Object[]
    // ================================================================================================================

    public static <T> T[] slice(final T[] array, int start) {
        return slice(array, start, array.length);
    }

    public static <T> T[] slice(final T[] array, int start, int stop) {
        return slice(array, start, stop, DEFAULT_STEP);
    }

    public static <T> T[] slice(final T[] array, int start, int stop, int step) {
        return arraySlice(array, start, stop, step);
    }

    public static <T> T[] slice(final T[] array, Integer start) {
        return slice(array, start, null);
    }

    public static <T> T[] slice(final T[] array, Integer start, Integer stop) {
        return slice(array, start, stop, null);
    }

    public static <T> T[] slice(final T[] array, Integer start, Integer stop, Integer step) {
        return arraySlice(array, start, stop, step);
    }

    // ================================================================================================================
// Slice: boolean[]
// ================================================================================================================

    public static boolean[] slice(final boolean[] array, int start) {
        return slice(array, start, array.length);
    }

    public static boolean[] slice(final boolean[] array, int start, int stop) {
        return slice(array, start, stop, DEFAULT_STEP);
    }

    public static boolean[] slice(final boolean[] array, int start, int stop, int step) {
        return arraySlice(array, start, stop, step);
    }

    public static boolean[] slice(final boolean[] array, Integer start) {
        return slice(array, start, null);
    }

    public static boolean[] slice(final boolean[] array, Integer start, Integer stop) {
        return slice(array, start, stop, null);
    }

    public static boolean[] slice(final boolean[] array, Integer start, Integer stop, Integer step) {
        return arraySlice(array, start, stop, step);
    }

// ================================================================================================================
// Slice: byte[]
// ================================================================================================================

    public static byte[] slice(final byte[] array, int start) {
        return slice(array, start, array.length);
    }

    public static byte[] slice(final byte[] array, int start, int stop) {
        return slice(array, start, stop, DEFAULT_STEP);
    }

    public static byte[] slice(final byte[] array, int start, int stop, int step) {
        return arraySlice(array, start, stop, step);
    }

    public static byte[] slice(final byte[] array, Integer start) {
        return slice(array, start, null);
    }

    public static byte[] slice(final byte[] array, Integer start, Integer stop) {
        return slice(array, start, stop, null);
    }

    public static byte[] slice(final byte[] array, Integer start, Integer stop, Integer step) {
        return arraySlice(array, start, stop, step);
    }

// ================================================================================================================
// Slice: short[]
// ================================================================================================================

    public static short[] slice(final short[] array, int start) {
        return slice(array, start, array.length);
    }

    public static short[] slice(final short[] array, int start, int stop) {
        return slice(array, start, stop, DEFAULT_STEP);
    }

    public static short[] slice(final short[] array, int start, int stop, int step) {
        return arraySlice(array, start, stop, step);
    }

    public static short[] slice(final short[] array, Integer start) {
        return slice(array, start, null);
    }

    public static short[] slice(final short[] array, Integer start, Integer stop) {
        return slice(array, start, stop, null);
    }

    public static short[] slice(final short[] array, Integer start, Integer stop, Integer step) {
        return arraySlice(array, start, stop, step);
    }

// ================================================================================================================
// Slice: int[]
// ================================================================================================================

    public static int[] slice(final int[] array, int start) {
        return slice(array, start, array.length);
    }

    public static int[] slice(final int[] array, int start, int stop) {
        return slice(array, start, stop, DEFAULT_STEP);
    }

    public static int[] slice(final int[] array, int start, int stop, int step) {
        return arraySlice(array, start, stop, step);
    }

    public static int[] slice(final int[] array, Integer start) {
        return slice(array, start, null);
    }

    public static int[] slice(final int[] array, Integer start, Integer stop) {
        return slice(array, start, stop, null);
    }

    public static int[] slice(final int[] array, Integer start, Integer stop, Integer step) {
        return arraySlice(array, start, stop, step);
    }

// ================================================================================================================
// Slice: long[]
// ================================================================================================================

    public static long[] slice(final long[] array, int start) {
        return slice(array, start, array.length);
    }

    public static long[] slice(final long[] array, int start, int stop) {
        return slice(array, start, stop, DEFAULT_STEP);
    }

    public static long[] slice(final long[] array, int start, int stop, int step) {
        return arraySlice(array, start, stop, step);
    }

    public static long[] slice(final long[] array, Integer start) {
        return slice(array, start, null);
    }

    public static long[] slice(final long[] array, Integer start, Integer stop) {
        return slice(array, start, stop, null);
    }

    public static long[] slice(final long[] array, Integer start, Integer stop, Integer step) {
        return arraySlice(array, start, stop, step);
    }

// ================================================================================================================
// Slice: float[]
// ================================================================================================================

    public static float[] slice(final float[] array, int start) {
        return slice(array, start, array.length);
    }

    public static float[] slice(final float[] array, int start, int stop) {
        return slice(array, start, stop, DEFAULT_STEP);
    }

    public static float[] slice(final float[] array, int start, int stop, int step) {
        return arraySlice(array, start, stop, step);
    }

    public static float[] slice(final float[] array, Integer start) {
        return slice(array, start, null);
    }

    public static float[] slice(final float[] array, Integer start, Integer stop) {
        return slice(array, start, stop, null);
    }

    public static float[] slice(final float[] array, Integer start, Integer stop, Integer step) {
        return arraySlice(array, start, stop, step);
    }

// ================================================================================================================
// Slice: double[]
// ================================================================================================================

    public static double[] slice(final double[] array, int start) {
        return slice(array, start, array.length);
    }

    public static double[] slice(final double[] array, int start, int stop) {
        return slice(array, start, stop, DEFAULT_STEP);
    }

    public static double[] slice(final double[] array, int start, int stop, int step) {
        return arraySlice(array, start, stop, step);
    }

    public static double[] slice(final double[] array, Integer start) {
        return slice(array, start, null);
    }

    public static double[] slice(final double[] array, Integer start, Integer stop) {
        return slice(array, start, stop, null);
    }

    public static double[] slice(final double[] array, Integer start, Integer stop, Integer step) {
        return arraySlice(array, start, stop, step);
    }

// ================================================================================================================
// Slice: char[]
// ================================================================================================================

    public static char[] slice(final char[] array, int start) {
        return slice(array, start, array.length);
    }

    public static char[] slice(final char[] array, int start, int stop) {
        return slice(array, start, stop, DEFAULT_STEP);
    }

    public static char[] slice(final char[] array, int start, int stop, int step) {
        return arraySlice(array, start, stop, step);
    }

    public static char[] slice(final char[] array, Integer start) {
        return slice(array, start, null);
    }

    public static char[] slice(final char[] array, Integer start, Integer stop) {
        return slice(array, start, stop, null);
    }

    public static char[] slice(final char[] array, Integer start, Integer stop, Integer step) {
        return arraySlice(array, start, stop, step);
    }

    // ================================================================================================================
    // Slice: Array internal
    // ================================================================================================================

    static <T> T arraySlice(final Object array, Integer start, Integer stop, Integer step) {
        return arraySlice(array, new SliceArguments(start, stop, step));
    }

    static <T> T arraySlice(final Object array, final SliceArguments args) {
        checkIfArray(array);

        Object result;
        args.sanitizeMembers(size(array));

        if (args.hasOpenStopAndNegativeStep()) {
            int resultSize = args.getSizeForNegativeAllLengthSlice();
            result = loopBasedArraySlice(array, args.start(), resultSize, args.step());

        } else {
            result = arraySlice(array, args.start(), args.stop(), args.step());
        }

        return (T) result;
    }

    static <T> T arraySlice(final Object array, int start, int stop, int step) {
        checkIfArray(array);
        checkIfStepNull(step);

        int size = size(array);
        start = limitValue(normalizeIndex(start, size), size - 1);
        stop = limitValue(normalizeIndex(stop, size), size);
        Class<?> componentType = getComponentType(array);

        final Object result;
        if (size == 0 || LoopUtils.isEmptyRange(start, stop, step)) {
            result = createEmptyArray(componentType);
        } else if (step == DEFAULT_STEP) {
            result = copy(array, start, stop);
        } else {
            int resultSize = LoopUtils.sizeOfRange(start, stop, step);
           result = loopBasedArraySlice(array, start, resultSize, step);
        }

        return (T) result;
    }

    static Object loopBasedArraySlice(final Object array, int start, int resultSize, int step) {
        checkIfArray(array);

        final Class<?> componentType = getComponentType(array);
        final Object result = ArrayUtils.createArray(componentType, resultSize);
        int resultIndex = 0;

        for(int i = 0; i < resultSize; ++i) {
            int rangeIndex = start + i * step;
            if(rangeIndex < 0 || size(array) <= rangeIndex) {
                continue;
            }

            Array.set(result, resultIndex, Array.get(array, rangeIndex));
            ++resultIndex;
        }

        return result;
    }

    static int limitValue(int value, int threshold) {
        int result = value;
        if(result > threshold) {
            result = threshold;
        }
        return result;
    }

    static void checkIfStepNull(int step) {
        if (step == 0) {
            throw new IllegalArgumentException("'step' must be non-zero.");
        }
    }
}

class SliceArguments {
    private Integer start;
    private Integer stop;
    private Integer step;
    private boolean normalized;

    SliceArguments(Integer start, Integer stop, Integer step) {
        this.start = start;
        this.stop = stop;
        this.step = step;
    }

    void sanitizeMembers(int size) {
        if (!normalized) {
            step = Optional.ofNullable(step).orElse(AccessUtils.DEFAULT_STEP);

            if (step >= 0) {
                start = Optional.ofNullable(start).orElse(0);
                stop = Optional.ofNullable(stop).orElse(size);
                stop = limitValue(normalizeIndex(stop, size), size);
            } else {
                start = Optional.ofNullable(start).orElse(size - 1);
            }

            start = limitValue(normalizeIndex(start, size), size - 1);
            normalized = true;
        } else {
            throw new IllegalStateException("SliceArguments were already sanitized.");
        }
    }

    boolean hasOpenStopAndNegativeStep() {
        if (!normalized) {
            throw new IllegalStateException("SliceArguments must be sanitized first.");
        }
        return stop == null;
    }

    int getSizeForNegativeAllLengthSlice() {
        if (!normalized) {
            throw new IllegalStateException("SliceArguments must be sanitized first.");
        }
        return LoopUtils.sizeOfRange(start(), -1, step());
    }

    int start() {
        return start;
    }

    int stop() {
        return stop;
    }

    int step() {
        return step;
    }
}