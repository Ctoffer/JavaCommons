package de.ctoffer.commons.container;

import de.ctoffer.commons.test.util.Expect;
import de.ctoffer.commons.test.util.TestCase;
import de.ctoffer.commons.test.util.TestStep;
import de.ctoffer.commons.test.util.TestSubject;
import org.junit.jupiter.api.*;

import java.util.*;
import java.util.stream.IntStream;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

class ccessUtilsTest {
    @Nested
    @DisplayName("Test AccessUtils#get")
    class TestGet {
        @Nested
        @DisplayName("Test With List")
        class WithList {
            private List<Integer> intList;
            private List<Integer> intListReversed;

            @BeforeEach
            void setup() {
                intList = Arrays.asList(1, 1, 2, 3, 5, 8, 13, 21);
                intListReversed = new ArrayList<>(intList);
                Collections.reverse(intListReversed);
            }

            @Test
            @DisplayName("Test All Valid Positive Indices")
            void testValidPositiveIndices() {
                TestSubject<List<Integer>> positiveIndexResult = new TestSubject<>();

                TestCase.assumeThat(intList.isEmpty(), TestStep.IS_FALSE)

                        .testStep(() -> positiveIndexResult.setSubject(
                                IntStream.range(0, intList.size())
                                        .mapToObj(i -> AccessUtils.get(intList, i))
                                        .collect(toList())
                                )
                        )

                        .resultingInSubject(positiveIndexResult, TestStep.IsNotNull())
                        .resultingInSubject(positiveIndexResult, TestStep.Where(List::size, TestStep.Equals(intList.size())))
                        .resultingInSubject(positiveIndexResult, TestStep.WhereEachEntry(TestStep.EqualsEntryOf(intList)));
            }

            @Test
            @DisplayName("Test All Valid Negative Indices")
            void testValidNegativeIndices() {
                TestSubject<List<Integer>> negativeIndexResult = new TestSubject<>();

                TestCase.assumeThat(intList.isEmpty(), TestStep.IS_FALSE)
                        .assumeThat(intListReversed.isEmpty(), TestStep.IS_FALSE)

                        .testStep(() -> negativeIndexResult.setSubject(
                                IntStream.rangeClosed(1, intList.size())
                                        .map(i -> -i)
                                        .mapToObj(i -> AccessUtils.get(intList, i))
                                        .collect(toList())
                                )
                        )

                        .resultingInSubject(negativeIndexResult, TestStep.IsNotNull())
                        .resultingInSubject(negativeIndexResult, TestStep.Where(List::size, TestStep.Equals(intListReversed.size())))
                        .resultingInSubject(negativeIndexResult, TestStep.WhereEachEntry(TestStep.EqualsEntryOf(intListReversed)));
            }

            @Test
            @DisplayName("Test Invalid Positive Index")
            void testInvalidPositiveIndices() {
                final int positiveEdgeIndex = intList.size();
                final String positiveExceptionMessage = "Index " + positiveEdgeIndex
                        + " is out of bounds for ArrayList of size "
                        + intList.size();

                TestCase.assumeThat(intList.isEmpty(), TestStep.IS_FALSE)

                        .testStep(() -> AccessUtils.get(intList, positiveEdgeIndex), Expect.Expecting(
                                IndexOutOfBoundsException.class,
                                positiveExceptionMessage
                        ));
            }

            @Test
            @DisplayName("Test Invalid Negative Index")
            void testInvalidNegativeIndices() {
                final int negativeEdgeIndex = -(intList.size() + 1);
                final String negativeExceptionMessage = "Index " + negativeEdgeIndex
                        + " is out of bounds for ArrayList of size "
                        + intList.size();

                TestCase.assumeThat(intList.isEmpty(), TestStep.IS_FALSE)

                        .testStep(() -> AccessUtils.get(intList, negativeEdgeIndex), Expect.Expecting(
                                IndexOutOfBoundsException.class,
                                negativeExceptionMessage
                        ));
            }
        }

        @Nested
        @DisplayName("Test With String")
        class WithString {
            private CharSequence alphabet;
            private CharSequence alphabetReversed;

            @BeforeEach
            void setup() {
                alphabet = "abcdefghjiklmnopqrstuvxyz";
                alphabetReversed = new StringBuilder(alphabet).reverse().toString();
            }

            @Test
            @DisplayName("Test All valid positive Indices")
            void testValidPositiveIndices() {
                TestSubject<CharSequence> positiveIndexResult = new TestSubject<>();

                TestCase.assumeThat(alphabet.length(), TestStep.GreaterThan(0))

                        .testStep(() -> positiveIndexResult.setSubject(
                                IntStream.range(0, alphabet.length())
                                        .mapToObj(i -> AccessUtils.get(alphabet, i))
                                        .map(Objects::toString)
                                        .collect(joining())
                                )
                        )

                        .resultingInSubject(positiveIndexResult, TestStep.IsNotNull())
                        .resultingInSubject(positiveIndexResult, TestStep.Where(CharSequence::length, TestStep.Equals(alphabet.length())))
                        .resultingInSubject(positiveIndexResult, TestStep.Equals(alphabet));
            }

            @Test
            @DisplayName("Test All valid negative Indices")
            void testValidNegativeIndices() {
                TestSubject<CharSequence> negativeIndexResult = new TestSubject<>();

                TestCase.assumeThat(alphabet.length(), TestStep.GreaterThan(0))
                        .assumeThat(alphabetReversed.length(), TestStep.GreaterThan(0))

                        .testStep(() -> negativeIndexResult.setSubject(
                                IntStream.rangeClosed(1, alphabet.length())
                                        .map(i -> -i)
                                        .mapToObj(i -> AccessUtils.get(alphabet, i))
                                        .map(Objects::toString)
                                        .collect(joining())
                                )
                        )

                        .resultingInSubject(negativeIndexResult, TestStep.IsNotNull())
                        .resultingInSubject(negativeIndexResult, TestStep.Where(CharSequence::length, TestStep.Equals(alphabetReversed.length())))
                        .resultingInSubject(negativeIndexResult, TestStep.Equals(alphabetReversed));
            }

            @Test
            @DisplayName("Test Invalid positive Index")
            void testInvalidPositiveIndices() {
                final int positiveEdgeIndex = alphabet.length();
                final String positiveExceptionMessage = "Index " + positiveEdgeIndex
                        + " is out of bounds for String of size "
                        + alphabet.length();

                TestCase.assumeThat(alphabet.length(), TestStep.GreaterThan(0))

                        .testStep(() -> AccessUtils.get(alphabet, positiveEdgeIndex), Expect.Expecting(
                                IndexOutOfBoundsException.class,
                                positiveExceptionMessage
                        ));
            }

            @Test
            @DisplayName("Test Invalid negative Index")
            void testInvalidNegativeIndices() {
                final int negativeEdgeIndex = -(alphabet.length() + 1);
                final String negativeExceptionMessage = "Index " + negativeEdgeIndex
                        + " is out of bounds for String of size "
                        + alphabet.length();

                TestCase.assumeThat(alphabet.length(), TestStep.GreaterThan(0))

                        .testStep(() -> AccessUtils.get(alphabet, negativeEdgeIndex), Expect.Expecting(
                                IndexOutOfBoundsException.class,
                                negativeExceptionMessage
                        ));
            }
        }

        @Nested
        @DisplayName("Test With Object[]")
        class WithObjectArray {
            private Object[] objects;
            private Object[] objectsReversed;

            @BeforeEach
            void setup() {
                objects = new Object[]{
                        "Test",
                        42,
                        true,
                        -2.35711
                };
                objectsReversed = new Object[objects.length];
                for (int i = 0; i < objects.length; ++i) {
                    objectsReversed[i] = objects[objects.length - 1 - i];
                }
            }

            @Test
            @DisplayName("Test All valid positive Indices")
            void testValidPositiveIndices() {
                TestSubject<Object[]> positiveIndexResult = new TestSubject<>();

                TestCase.assumeThat(objects.length, TestStep.GreaterThan(0))

                        .testStep(() -> positiveIndexResult.setSubject(
                                IntStream.range(0, objects.length)
                                        .mapToObj(i -> AccessUtils.get(objects, i))
                                        .toArray()
                                )
                        )

                        .resultingInSubject(positiveIndexResult, TestStep.IsNotNull())
                        .resultingInSubject(positiveIndexResult, TestStep.Where(arr -> arr.length, TestStep.Equals(objects.length)))
                        .resultingInSubject(positiveIndexResult, TestStep.Equals(objects));
            }

            @Test
            @DisplayName("Test All valid negative Indices")
            void testValidNegativeIndices() {
                TestSubject<Object[]> negativeIndexResult = new TestSubject<>();

                TestCase.assumeThat(objects.length, TestStep.GreaterThan(0))
                        .assumeThat(objectsReversed.length, TestStep.GreaterThan(0))

                        .testStep(() -> negativeIndexResult.setSubject(
                                IntStream.rangeClosed(1, objects.length)
                                        .map(i -> -i)
                                        .mapToObj(i -> AccessUtils.get(objects, i))
                                        .toArray()
                                )
                        )

                        .resultingInSubject(negativeIndexResult, TestStep.IsNotNull())
                        .resultingInSubject(negativeIndexResult, TestStep.Where(arr -> arr.length, TestStep.Equals(objectsReversed.length)))
                        .resultingInSubject(negativeIndexResult, TestStep.Equals(objectsReversed));
            }

            @Test
            @DisplayName("Test Invalid positive Index")
            void testInvalidPositiveIndices() {
                final int positiveEdgeIndex = objects.length;
                final String positiveExceptionMessage = "Index " + positiveEdgeIndex
                        + " is out of bounds for Object[] of size "
                        + objects.length;

                TestCase.assumeThat(objects.length, TestStep.GreaterThan(0))

                        .testStep(() -> AccessUtils.get(objects, positiveEdgeIndex), Expect.Expecting(
                                IndexOutOfBoundsException.class,
                                positiveExceptionMessage
                        ));
            }

            @Test
            @DisplayName("Test Invalid negative Index")
            void testInvalidNegativeIndices() {
                final int negativeEdgeIndex = -(objects.length + 1);
                final String negativeExceptionMessage = "Index " + negativeEdgeIndex
                        + " is out of bounds for Object[] of size "
                        + objects.length;

                TestCase.assumeThat(objects.length, TestStep.GreaterThan(0))

                        .testStep(() -> AccessUtils.get(objects, negativeEdgeIndex), Expect.Expecting(
                                IndexOutOfBoundsException.class,
                                negativeExceptionMessage
                        ));
            }
        }

        @Nested
        @DisplayName("Test With int[]")
        class WithIntArray {
            private int[] integerArray;
            private int[] integerArrayReversed;

            @BeforeEach
            void setup() {
                integerArray = new int[]{1, 1, 2, 3, 5, 8, 13, 21};
                integerArrayReversed = new int[integerArray.length];
                for (int i = 0; i < integerArray.length; ++i) {
                    integerArrayReversed[i] = integerArray[integerArray.length - 1 - i];
                }
            }

            @Test
            @DisplayName("Test All valid positive Indices")
            void testValidPositiveIndices() {
                TestSubject<int[]> positiveIndexResult = new TestSubject<>();

                TestCase.assumeThat(integerArray.length, TestStep.GreaterThan(0))

                        .testStep(() -> positiveIndexResult.setSubject(
                                IntStream.range(0, integerArray.length)
                                        .map(i -> AccessUtils.get(integerArray, i))
                                        .toArray()
                                )
                        )

                        .resultingInSubject(positiveIndexResult, TestStep.IsNotNull())
                        .resultingInSubject(positiveIndexResult, TestStep.Where(arr -> arr.length, TestStep.Equals(integerArray.length)))
                        .resultingInSubject(positiveIndexResult, TestStep.Equals(integerArray));
            }

            @Test
            @DisplayName("Test All valid negative Indices")
            void testValidNegativeIndices() {
                TestSubject<int[]> negativeIndexResult = new TestSubject<>();

                TestCase.assumeThat(integerArray.length, TestStep.GreaterThan(0))
                        .assumeThat(integerArrayReversed.length, TestStep.GreaterThan(0))

                        .testStep(() -> negativeIndexResult.setSubject(
                                IntStream.rangeClosed(1, integerArray.length)
                                        .map(i -> -i)
                                        .map(i -> AccessUtils.get(integerArray, i))
                                        .toArray()
                                )
                        )

                        .resultingInSubject(negativeIndexResult, TestStep.IsNotNull())
                        .resultingInSubject(negativeIndexResult, TestStep.Where(arr -> arr.length, TestStep.Equals(integerArrayReversed.length)))
                        .resultingInSubject(negativeIndexResult, TestStep.Equals(integerArrayReversed));
            }

            @Test
            @DisplayName("Test Invalid positive Index")
            void testInvalidPositiveIndices() {
                final int positiveEdgeIndex = integerArray.length;
                final String positiveExceptionMessage = "Index " + positiveEdgeIndex
                        + " is out of bounds for int[] of size "
                        + integerArray.length;

                TestCase.assumeThat(integerArray.length, TestStep.GreaterThan(0))

                        .testStep(() -> AccessUtils.get(integerArray, positiveEdgeIndex), Expect.Expecting(
                                IndexOutOfBoundsException.class,
                                positiveExceptionMessage
                        ));
            }

            @Test
            @DisplayName("Test Invalid negative Index")
            void testInvalidNegativeIndices() {
                final int negativeEdgeIndex = -(integerArray.length + 1);
                final String negativeExceptionMessage = "Index " + negativeEdgeIndex
                        + " is out of bounds for int[] of size "
                        + integerArray.length;

                TestCase.assumeThat(integerArray.length, TestStep.GreaterThan(0))

                        .testStep(() -> AccessUtils.get(integerArray, negativeEdgeIndex), Expect.Expecting(
                                IndexOutOfBoundsException.class,
                                negativeExceptionMessage
                        ));
            }
        }
    }

    @Nested
    @DisplayName("Test AccessUtils#size")
    class TestSize {
        @Nested
        @DisplayName("Test With Collection")
        class WithCollection {
            @Test
            void testEmpty() {
                Collection<Object> collection = emptyList();
                TestSubject<Integer> actualSize = new TestSubject<>();
                TestCase.assumeThat(collection.size(), TestStep.Equals(0))

                        .testStep(actualSize, () -> AccessUtils.size(collection))

                        .resultingInSubject(actualSize, TestStep.Equals(collection.size()));
            }

            @RepeatedTest(10)
            void testRandomSize() {
                int expectedSize = 1 + Math.abs(new Random(System.currentTimeMillis()).nextInt(100));
                Collection<Object> collection = IntStream.range(0, expectedSize).boxed().collect(toList());
                TestSubject<Integer> actualSize = new TestSubject<>();

                TestCase.assumeThat(expectedSize, TestStep.GreaterThan(0))

                        .testStep(actualSize, () -> AccessUtils.size(collection))

                        .resultingInSubject(actualSize, TestStep.Equals(expectedSize));
            }
        }

        @Nested
        @DisplayName("Test With Object[]")
        class WithObjectArray {
            @Test
            void testEmpty() {
                Object[] array = new Object[]{};
                TestSubject<Integer> actualSize = new TestSubject<>();
                TestCase.assumeThat(array.length, TestStep.Equals(0))

                        .testStep(actualSize, () -> AccessUtils.size(array))

                        .resultingInSubject(actualSize, TestStep.Equals(array.length));
            }

            @RepeatedTest(10)
            void testRandomSize() {
                int expectedSize = 1 + Math.abs(new Random(System.currentTimeMillis()).nextInt(100));
                Object[] array = new Object[expectedSize];
                TestSubject<Integer> actualSize = new TestSubject<>();

                TestCase.assumeThat(expectedSize, TestStep.GreaterThan(0))

                        .testStep(actualSize, () -> AccessUtils.size(array))

                        .resultingInSubject(actualSize, TestStep.Equals(expectedSize));
            }
        }

        @Nested
        @DisplayName("Test With boolean[]")
        class WithBooleanArray {
            @Test
            void testEmpty() {
                boolean[] array = new boolean[]{};
                TestSubject<Integer> actualSize = new TestSubject<>();

                TestCase.assumeThat(array.length, TestStep.Equals(0))

                        .testStep(actualSize, () -> AccessUtils.size(array))

                        .resultingInSubject(actualSize, TestStep.Equals(array.length));
            }

            @RepeatedTest(10)
            void testRandomSize() {
                int expectedSize = 1 + Math.abs(new Random(System.currentTimeMillis()).nextInt(100));
                boolean[] array = new boolean[expectedSize];
                TestSubject<Integer> actualSize = new TestSubject<>();

                TestCase.assumeThat(expectedSize, TestStep.GreaterThan(0))

                        .testStep(actualSize, () -> AccessUtils.size(array))

                        .resultingInSubject(actualSize, TestStep.Equals(expectedSize));
            }
        }

        @Nested
        @DisplayName("Test With byte[]")
        class WithByteArray {
            @Test
            void testEmpty() {
                byte[] array = new byte[]{};
                TestSubject<Integer> actualSize = new TestSubject<>();

                TestCase.assumeThat(array.length, TestStep.Equals(0))

                        .testStep(actualSize, () -> AccessUtils.size(array))

                        .resultingInSubject(actualSize, TestStep.Equals(array.length));
            }

            @RepeatedTest(10)
            void testRandomSize() {
                int expectedSize = 1 + Math.abs(new Random(System.currentTimeMillis()).nextInt(100));
                byte[] array = new byte[expectedSize];
                TestSubject<Integer> actualSize = new TestSubject<>();

                TestCase.assumeThat(expectedSize, TestStep.GreaterThan(0))

                        .testStep(actualSize, () -> AccessUtils.size(array))

                        .resultingInSubject(actualSize, TestStep.Equals(expectedSize));
            }
        }

        @Nested
        @DisplayName("Test With short[]")
        class WithShortArray {
            @Test
            void testEmpty() {
                short[] array = new short[]{};
                TestSubject<Integer> actualSize = new TestSubject<>();

                TestCase.assumeThat(array.length, TestStep.Equals(0))

                        .testStep(actualSize, () -> AccessUtils.size(array))

                        .resultingInSubject(actualSize, TestStep.Equals(array.length));
            }

            @RepeatedTest(10)
            void testRandomSize() {
                int expectedSize = 1 + Math.abs(new Random(System.currentTimeMillis()).nextInt(100));
                short[] array = new short[expectedSize];
                TestSubject<Integer> actualSize = new TestSubject<>();

                TestCase.assumeThat(expectedSize, TestStep.GreaterThan(0))

                        .testStep(actualSize, () -> AccessUtils.size(array))

                        .resultingInSubject(actualSize, TestStep.Equals(expectedSize));
            }
        }

        @Nested
        @DisplayName("Test With int[]")
        class WithIntArray {
            @Test
            void testEmpty() {
                int[] array = new int[]{};
                TestSubject<Integer> actualSize = new TestSubject<>();

                TestCase.assumeThat(array.length, TestStep.Equals(0))

                        .testStep(actualSize, () -> AccessUtils.size(array))

                        .resultingInSubject(actualSize, TestStep.Equals(array.length));
            }

            @RepeatedTest(10)
            void testRandomSize() {
                int expectedSize = 1 + Math.abs(new Random(System.currentTimeMillis()).nextInt(100));
                int[] array = new int[expectedSize];
                TestSubject<Integer> actualSize = new TestSubject<>();

                TestCase.assumeThat(expectedSize, TestStep.GreaterThan(0))

                        .testStep(actualSize, () -> AccessUtils.size(array))

                        .resultingInSubject(actualSize, TestStep.Equals(expectedSize));
            }
        }

        @Nested
        @DisplayName("Test With long[]")
        class WithLongArray {
            @Test
            void testEmpty() {
                long[] array = new long[]{};
                TestSubject<Integer> actualSize = new TestSubject<>();

                TestCase.assumeThat(array.length, TestStep.Equals(0))

                        .testStep(actualSize, () -> AccessUtils.size(array))

                        .resultingInSubject(actualSize, TestStep.Equals(array.length));
            }

            @RepeatedTest(10)
            void testRandomSize() {
                int expectedSize = 1 + Math.abs(new Random(System.currentTimeMillis()).nextInt(100));
                long[] array = new long[expectedSize];
                TestSubject<Integer> actualSize = new TestSubject<>();

                TestCase.assumeThat(expectedSize, TestStep.GreaterThan(0))

                        .testStep(actualSize, () -> AccessUtils.size(array))

                        .resultingInSubject(actualSize, TestStep.Equals(expectedSize));
            }
        }

        @Nested
        @DisplayName("Test With float[]")
        class WithFloatArray {
            @Test
            void testEmpty() {
                float[] array = new float[]{};
                TestSubject<Integer> actualSize = new TestSubject<>();

                TestCase.assumeThat(array.length, TestStep.Equals(0))

                        .testStep(actualSize, () -> AccessUtils.size(array))

                        .resultingInSubject(actualSize, TestStep.Equals(array.length));
            }

            @RepeatedTest(10)
            void testRandomSize() {
                int expectedSize = 1 + Math.abs(new Random(System.currentTimeMillis()).nextInt(100));
                float[] array = new float[expectedSize];
                TestSubject<Integer> actualSize = new TestSubject<>();

                TestCase.assumeThat(expectedSize, TestStep.GreaterThan(0))

                        .testStep(actualSize, () -> AccessUtils.size(array))

                        .resultingInSubject(actualSize, TestStep.Equals(expectedSize));
            }
        }

        @Nested
        @DisplayName("Test With double[]")
        class WithDoubleArray {
            @Test
            void testEmpty() {
                double[] array = new double[]{};
                TestSubject<Integer> actualSize = new TestSubject<>();

                TestCase.assumeThat(array.length, TestStep.Equals(0))

                        .testStep(actualSize, () -> AccessUtils.size(array))

                        .resultingInSubject(actualSize, TestStep.Equals(array.length));
            }

            @RepeatedTest(10)
            void testRandomSize() {
                int expectedSize = 1 + Math.abs(new Random(System.currentTimeMillis()).nextInt(100));
                double[] array = new double[expectedSize];
                TestSubject<Integer> actualSize = new TestSubject<>();

                TestCase.assumeThat(expectedSize, TestStep.GreaterThan(0))

                        .testStep(actualSize, () -> AccessUtils.size(array))

                        .resultingInSubject(actualSize, TestStep.Equals(expectedSize));
            }
        }

        @Nested
        @DisplayName("Test With char[]")
        class WithCharArray {
            @Test
            void testEmpty() {
                char[] array = new char[]{};
                TestSubject<Integer> actualSize = new TestSubject<>();

                TestCase.assumeThat(array.length, TestStep.Equals(0))

                        .testStep(actualSize, () -> AccessUtils.size(array))

                        .resultingInSubject(actualSize, TestStep.Equals(array.length));
            }

            @RepeatedTest(10)
            void testRandomSize() {
                int expectedSize = 1 + Math.abs(new Random(System.currentTimeMillis()).nextInt(100));
                char[] array = new char[expectedSize];
                TestSubject<Integer> actualSize = new TestSubject<>();

                TestCase.assumeThat(expectedSize, TestStep.GreaterThan(0))

                        .testStep(actualSize, () -> AccessUtils.size(array))

                        .resultingInSubject(actualSize, TestStep.Equals(expectedSize));
            }
        }
    }

    @Nested
    @DisplayName("Test AccessUtils#slice")
    class TestSlice {

        private Object[] emptyArray;
        private Object[] testArray;

        @BeforeEach
        void setup() {
            this.emptyArray = new Object[]{};
            this.testArray = new String[]{"a", "b", "c", "d", "e", "f"};
        }

        @Nested
        @DisplayName("Test With List")
        class WithList {
            void testWitZeroStep() {
            }

            void testWithEmptyArray() {
            }

            void testWithEmptyPositiveSlice() {
            }

            void testWithEmptyNegativeSlice() {
            }

            void testFullSlice() {
            }

            void testPartialSlice() {
            }

            void testPartialReverseSlice() {
            }
        }

        @Nested
        @DisplayName("Test With String")
        class WithString {
            void testWitZeroStep() {
            }

            void testWithEmptyArray() {
            }

            void testWithEmptyPositiveSlice() {
            }

            void testWithEmptyNegativeSlice() {
            }

            void testFullSlice() {
            }

            void testPartialSlice() {
            }

            void testPartialReverseSlice() {
            }
        }

        @Nested
        @DisplayName("Test With T[]")
        class WithGenericArray {
            @Test
            @DisplayName("slice([a, b, c, d, e, f], 0, 6, 0) -> Error")
            void testWitZeroStep() {
                int start = 0;
                int stop = testArray.length;
                int step = 0;
                TestCase.assumeThat(step, TestStep.Equals(0))

                        .testStep(() -> AccessUtils.slice(testArray, start, stop, step),
                                Expect.Expecting(IllegalArgumentException.class, "'step' must be non-zero.")
                        );
            }

            @Test
            @DisplayName("slice([], 0) -> []")
            void testWithEmptyArray() {
                TestSubject<Object[]> slicedArray = new TestSubject<>();
                TestCase.assumeThat(emptyArray.length, TestStep.Equals(0))

                        .testStep(() -> slicedArray.setSubject(AccessUtils.slice(emptyArray, 0)))

                        .resultingInSubject(slicedArray, TestStep.Where(AccessUtils::size, TestStep.Equals(0)));
            }

            @Test
            @DisplayName("slice([a, b, c, d, e, f], 3, 3, 1) -> []")
            void testWithEmptyPositiveSlice() {
                int start = 3;
                int stop = 3;
                int step = 1;
                TestSubject<Object[]> slicedArray = new TestSubject<>();

                TestCase.assumeThat(start, TestStep.Equals(stop))
                        .assumeThat(step, TestStep.GreaterThan(0))

                        .testStep(slicedArray, () -> AccessUtils.slice(testArray, start, stop, step))

                        .resultingInSubject(slicedArray, TestStep.Where(AccessUtils::size, TestStep.Equals(0)));
            }

            @Test
            @DisplayName("slice([a, b, c, d, e, f], 3, 3, -1) -> []")
            void testWithEmptyNegativeSlice() {
                int start = 3;
                int stop = 3;
                int step = -1;
                TestSubject<Object[]> slicedArray = new TestSubject<>();

                TestCase.assumeThat(start, TestStep.Equals(stop))
                        .assumeThat(step, TestStep.LessThan(0))

                        .testStep(slicedArray, () -> AccessUtils.slice(testArray, start, stop, step))

                        .resultingInSubject(slicedArray, TestStep.Where(AccessUtils::size, TestStep.Equals(0)));
            }

            @Test
            @DisplayName("slice([a, b, c, d, e, f], 0, 10) -> [a, b, c, d, e, f]")
            void testExcessSlice() {
                int start = 0;
                int stop = 10;
                TestSubject<Object[]> slicedArray = new TestSubject<>();

                TestCase.assumeThat(start, TestStep.Equals(0))
                        .assumeThat(stop, TestStep.GreaterThan(testArray.length))

                        .testStep(slicedArray, () -> AccessUtils.slice(testArray, start, stop))

                        .resultingInSubject(slicedArray, TestStep.Where(AccessUtils::size, TestStep.Equals(testArray.length)))
                        .resultingInSubject(slicedArray, TestStep.Where(ArrayUtils::toIterable, TestStep.WhereEachEntry(TestStep.EqualsEntryOf(testArray))))
                        .resultingInSubject(slicedArray, TestStep.HasDifferentReferenceAs(testArray))
                        .resultingInSubject(slicedArray, TestStep.HasDifferentHashAs(testArray));
            }

            @Test
            @DisplayName("slice([a, b, c, d, e, f], 10, 0, -1) -> [f, e, d, c, b]")
            void testExcessReverseSlice() {
                int start = 10;
                int stop = 0;
                int step = -1;
                TestSubject<Object[]> slicedArray = new TestSubject<>();
                Object[] expectedArray = new Object[]{testArray[5], testArray[4], testArray[3], testArray[2], testArray[1]};

                TestCase.assumeThat(start, TestStep.GreaterThan(testArray.length))
                        .assumeThat(stop, TestStep.Equals(0))
                        .assumeThat(step, TestStep.Equals(-1))

                        .testStep(slicedArray, () -> AccessUtils.slice(testArray, start, stop, step))

                        .resultingInSubject(slicedArray, TestStep.Where(AccessUtils::size, TestStep.Equals(expectedArray.length)))
                        .resultingInSubject(slicedArray, TestStep.Where(ArrayUtils::toIterable, TestStep.WhereEachEntry(TestStep.EqualsEntryOf(expectedArray))))
                        .resultingInSubject(slicedArray, TestStep.HasDifferentReferenceAs(expectedArray))
                        .resultingInSubject(slicedArray, TestStep.HasDifferentHashAs(expectedArray));
            }


            @Test
            @DisplayName("slice([a, b, c, d, e, f], 0, 6) -> [a, b, c, d, e, f]")
            void testFullSlice() {
                int start = 0;
                int stop = testArray.length;
                TestSubject<Object[]> slicedArray = new TestSubject<>();

                TestCase.assumeThat(start, TestStep.Equals(0))
                        .assumeThat(stop, TestStep.Equals(testArray.length))

                        .testStep(slicedArray, () -> AccessUtils.slice(testArray, start, stop))

                        .resultingInSubject(slicedArray, TestStep.Where(AccessUtils::size, TestStep.Equals(testArray.length)))
                        .resultingInSubject(slicedArray, TestStep.Where(ArrayUtils::toIterable, TestStep.WhereEachEntry(TestStep.EqualsEntryOf(testArray))))
                        .resultingInSubject(slicedArray, TestStep.HasDifferentReferenceAs(testArray))
                        .resultingInSubject(slicedArray, TestStep.HasDifferentHashAs(testArray));
            }

            @Test
            @DisplayName("slice([a, b, c, d, e, f], 5, 0, -1) -> [f, e, d, c, b]")
            void testFullReverseSlice() {
                int start = testArray.length - 1;
                int stop = 0;
                int step = -1;
                TestSubject<Object[]> slicedArray = new TestSubject<>();
                Object[] expectedArray = new Object[]{testArray[5], testArray[4], testArray[3], testArray[2], testArray[1]};

                TestCase.assumeThat(start, TestStep.Equals(testArray.length - 1))
                        .assumeThat(stop, TestStep.Equals(0))
                        .assumeThat(step, TestStep.Equals(-1))

                        .testStep(slicedArray, () -> AccessUtils.slice(testArray, start, stop, step))

                        .resultingInSubject(slicedArray, TestStep.Where(AccessUtils::size, TestStep.Equals(expectedArray.length)))
                        .resultingInSubject(slicedArray, TestStep.Where(ArrayUtils::toIterable, TestStep.WhereEachEntry(TestStep.EqualsEntryOf(expectedArray))))
                        .resultingInSubject(slicedArray, TestStep.HasDifferentReferenceAs(expectedArray))
                        .resultingInSubject(slicedArray, TestStep.HasDifferentHashAs(expectedArray));
            }

            @Test
            @DisplayName("slice([a, b, c, d, e, f], 0, 3) -> [a, b, c]")
            void testPartialSlice() {
                TestSubject<Object[]> actualResult = new TestSubject<>();
                Object[] expectedResult = new Object[]{testArray[0], testArray[1], testArray[2]};
                int start = 0;
                int stop = 3;

                TestCase.assumeThat(start, TestStep.LessThan(stop))

                        .testStep(actualResult, () -> AccessUtils.slice(testArray, start, stop))

                        .resultingInSubject(actualResult, TestStep.Where(AccessUtils::size, TestStep.Equals(expectedResult.length)))
                        .resultingInSubject(actualResult, TestStep.Where(ArrayUtils::toIterable, TestStep.WhereEachEntry(TestStep.EqualsEntryOf(expectedResult))));
            }

            @Test
            @DisplayName("slice([a, b, c, d, e, f], 0, -3) -> [a, b, c]")
            void testPartialNegativeEndSlice() {
                TestSubject<Object[]> actualResult = new TestSubject<>();
                Object[] expectedResult = new Object[]{testArray[0], testArray[1], testArray[2]};
                int start = 0;
                int stop = -3;

                TestCase.assumeThat(stop, TestStep.LessThan(0))

                        .testStep(actualResult, () -> AccessUtils.slice(testArray, start, stop))

                        .resultingInSubject(actualResult, TestStep.Where(AccessUtils::size, TestStep.Equals(expectedResult.length)))
                        .resultingInSubject(actualResult, TestStep.Where(ArrayUtils::toIterable, TestStep.WhereEachEntry(TestStep.EqualsEntryOf(expectedResult))));
            }

            @Test
            @DisplayName("slice([a, b, c, d, e, f], 5, 3, -1) -> [f, e]")
            void testPartialReverseSlice() {
                TestSubject<Object[]> actualResult = new TestSubject<>();
                Object[] expectedResult = new Object[]{testArray[5], testArray[4]};
                int start = testArray.length - 1;
                int stop = 3;
                int step = -1;

                TestCase.assumeThat(start, TestStep.GreaterThan(stop))
                        .assumeThat(start, TestStep.Equals(testArray.length - 1))
                        .assumeThat(step, TestStep.LessThan(0))

                        .testStep(actualResult, () -> AccessUtils.slice(testArray, start, stop, step))

                        .resultingInSubject(actualResult, TestStep.Where(AccessUtils::size, TestStep.Equals(expectedResult.length)))
                        .resultingInSubject(actualResult, TestStep.Where(ArrayUtils::toIterable, TestStep.WhereEachEntry(TestStep.EqualsEntryOf(expectedResult))));
            }

            @Test
            @DisplayName("slice([a, b, c, d, e, f], -1, 3, -1) -> [f, e]")
            void testPartialReverseNegativeEndSlice() {
                TestSubject<Object[]> actualResult = new TestSubject<>();
                Object[] expectedResult = new Object[]{testArray[5], testArray[4]};
                int start = -1;
                int stop = 3;
                int step = -1;

                TestCase.assumeThat(start, TestStep.LessThan(0))
                        .assumeThat(step, TestStep.Equals(-1))

                        .testStep(actualResult, () -> AccessUtils.slice(testArray, start, stop, step))

                        .resultingInSubject(actualResult, TestStep.Where(AccessUtils::size, TestStep.Equals(expectedResult.length)))
                        .resultingInSubject(actualResult, TestStep.Where(ArrayUtils::toIterable, TestStep.WhereEachEntry(TestStep.EqualsEntryOf(expectedResult))));
            }

            @Test
            @DisplayName("slice([a, b, c, d, e, f], 0, 6, null) -> [a, b, c, d, e, f]")
            void testSliceNoStep() {
                final TestSubject<Object[]> actual = new TestSubject<>();
                final Object[] expected = testArray;
                final Integer start = 0;
                final Integer stop = 6;
                final Integer step = null;

                TestCase.assumeThat(start, TestStep.Equals(0))
                        .assumeThat(stop, TestStep.Equals(6))
                        .assumeThat(step, TestStep.IsNull())

                        .testStep(actual, () -> AccessUtils.slice(testArray, start, stop, step))

                        .resultingInSubject(actual, TestStep.Where(AccessUtils::size, TestStep.Equals(expected.length)))
                        .resultingInSubject(actual, TestStep.Where(ArrayUtils::toIterable, TestStep.WhereEachEntry(TestStep.EqualsEntryOf(expected))))
                        .resultingInSubject(actual, TestStep.HasDifferentReferenceAs(expected))
                        .resultingInSubject(actual, TestStep.HasDifferentHashAs(expected));
            }

            @Test
            @DisplayName("slice([a, b, c, d, e, f], null, 6, 2) -> [a, c, e]")
            void testSliceNoStart() {
                final TestSubject<Object[]> actual = new TestSubject<>();
                final Object[] expected = new Object[]{testArray[0], testArray[2], testArray[4]};
                final Integer start = null;
                final Integer stop = 6;
                final Integer step = 2;

                TestCase.assumeThat(start, TestStep.IsNull())
                        .assumeThat(stop, TestStep.Equals(6))
                        .assumeThat(step, TestStep.Equals(2))

                        .testStep(actual, () -> AccessUtils.slice(testArray, start, stop, step))

                        .resultingInSubject(actual, TestStep.Where(AccessUtils::size, TestStep.Equals(expected.length)))
                        .resultingInSubject(actual, TestStep.Where(ArrayUtils::toIterable, TestStep.WhereEachEntry(TestStep.EqualsEntryOf(expected))))
                        .resultingInSubject(actual, TestStep.HasDifferentReferenceAs(expected))
                        .resultingInSubject(actual, TestStep.HasDifferentHashAs(expected));
            }

            @Test
            @DisplayName("slice([a, b, c, d, e, f], 0, null, 2) -> [a, c, e]")
            void testSliceNoStop() {
                final TestSubject<Object[]> actual = new TestSubject<>();
                final Object[] expected = new Object[]{testArray[0], testArray[2], testArray[4]};
                final Integer start = 0;
                final Integer stop = null;
                final Integer step = 2;

                TestCase.assumeThat(start, TestStep.Equals(0))
                        .assumeThat(stop, TestStep.IsNull())
                        .assumeThat(step, TestStep.Equals(2))

                        .testStep(actual, () -> AccessUtils.slice(testArray, start, stop, step))

                        .resultingInSubject(actual, TestStep.Where(AccessUtils::size, TestStep.Equals(expected.length)))
                        .resultingInSubject(actual, TestStep.Where(ArrayUtils::toIterable, TestStep.WhereEachEntry(TestStep.EqualsEntryOf(expected))))
                        .resultingInSubject(actual, TestStep.HasDifferentReferenceAs(expected))
                        .resultingInSubject(actual, TestStep.HasDifferentHashAs(expected));
            }

            @Test
            @DisplayName("slice([a, b, c, d, e, f], null, null, 2) -> [a, c, e]")
            void testSliceNoStartAndStop() {
                final TestSubject<Object[]> actual = new TestSubject<>();
                final Object[] expected = new Object[]{testArray[0], testArray[2], testArray[4]};
                final Integer start = null;
                final Integer stop = null;
                final Integer step = 2;

                TestCase.assumeThat(start, TestStep.IsNull())
                        .assumeThat(stop, TestStep.IsNull())
                        .assumeThat(step, TestStep.Equals(2))

                        .testStep(actual, () -> AccessUtils.slice(testArray, start, stop, step))

                        .resultingInSubject(actual, TestStep.Where(AccessUtils::size, TestStep.Equals(expected.length)))
                        .resultingInSubject(actual, TestStep.Where(ArrayUtils::toIterable, TestStep.WhereEachEntry(TestStep.EqualsEntryOf(expected))))
                        .resultingInSubject(actual, TestStep.HasDifferentReferenceAs(expected))
                        .resultingInSubject(actual, TestStep.HasDifferentHashAs(expected));
            }

            @Test
            @DisplayName("slice([a, b, c, d, e, f], null, 0, -1) -> [f, e, d, c, b]")
            void testReverseSliceNoStart() {
                final TestSubject<Object[]> actual = new TestSubject<>();
                final Object[] expected = new Object[]{testArray[5], testArray[4], testArray[3], testArray[2], testArray[1]};
                final Integer start = null;
                final Integer stop = 0;
                final Integer step = -1;

                TestCase.assumeThat(start, TestStep.IsNull())
                        .assumeThat(stop, TestStep.Equals(0))
                        .assumeThat(step, TestStep.Equals(-1))

                        .testStep(actual, () -> AccessUtils.slice(testArray, start, stop, step))

                        .resultingInSubject(actual, TestStep.Where(AccessUtils::size, TestStep.Equals(expected.length)))
                        .resultingInSubject(actual, TestStep.Where(ArrayUtils::toIterable, TestStep.WhereEachEntry(TestStep.EqualsEntryOf(expected))))
                        .resultingInSubject(actual, TestStep.HasDifferentReferenceAs(expected))
                        .resultingInSubject(actual, TestStep.HasDifferentHashAs(expected));
            }

            @Test
            @DisplayName("slice([a, b, c, d, e, f], 5, null, -1) -> [f, e, d, c, b, a]")
            void testReverseSliceNoStop() {
                final TestSubject<Object[]> actual = new TestSubject<>();
                final Object[] expected = new Object[]{testArray[5], testArray[4], testArray[3], testArray[2], testArray[1], testArray[0]};
                final Integer start = 5;
                final Integer stop = null;
                final Integer step = -1;

                TestCase.assumeThat(start, TestStep.Equals(5))
                        .assumeThat(stop, TestStep.IsNull())
                        .assumeThat(step, TestStep.Equals(-1))

                        .testStep(actual, () -> AccessUtils.slice(testArray, start, stop, step))

                        .resultingInSubject(actual, TestStep.Where(AccessUtils::size, TestStep.Equals(expected.length)))
                        .resultingInSubject(actual, TestStep.Where(ArrayUtils::toIterable, TestStep.WhereEachEntry(TestStep.EqualsEntryOf(expected))))
                        .resultingInSubject(actual, TestStep.HasDifferentReferenceAs(expected))
                        .resultingInSubject(actual, TestStep.HasDifferentHashAs(expected));
            }

            @Test
            @DisplayName("slice([a, b, c, d, e, f], null, null, -2) -> [f, d, b]")
            void testReverseSliceNoStartAndStop() {
                final TestSubject<Object[]> actual = new TestSubject<>();
                final Object[] expected = new Object[]{testArray[5], testArray[3], testArray[1]};
                final Integer start = null;
                final Integer stop = null;
                final Integer step = -2;

                TestCase.assumeThat(start, TestStep.IsNull())
                        .assumeThat(stop, TestStep.IsNull())
                        .assumeThat(step, TestStep.Equals(-2))

                        .testStep(actual, () -> AccessUtils.slice(testArray, start, stop, step))

                        .resultingInSubject(actual, TestStep.Where(AccessUtils::size, TestStep.Equals(expected.length)))
                        .resultingInSubject(actual, TestStep.Where(ArrayUtils::toIterable, TestStep.WhereEachEntry(TestStep.EqualsEntryOf(expected))))
                        .resultingInSubject(actual, TestStep.HasDifferentReferenceAs(expected))
                        .resultingInSubject(actual, TestStep.HasDifferentHashAs(expected));
            }
        }

        @Nested
        @DisplayName("Test With int[]")
        class WithPrimitiveIntArray {
            void testWitZeroStep() {
            }

            void testWithEmptyArray() {
            }

            void testWithEmptyPositiveSlice() {
            }

            void testWithEmptyNegativeSlice() {
            }

            void testFullSlice() {
            }

            void testPartialSlice() {
            }

            void testPartialReverseSlice() {
            }
        }
    }
}
