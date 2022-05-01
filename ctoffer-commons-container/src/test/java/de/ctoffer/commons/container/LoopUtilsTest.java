package de.ctoffer.commons.container;

import de.ctoffer.commons.test.util.Expect;
import de.ctoffer.commons.test.util.TestCase;
import de.ctoffer.commons.test.util.TestStep;
import de.ctoffer.commons.test.util.TestSubject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class LoopUtilsTest {
    @Nested
    @DisplayName("Test range(stop)")
    class TestRangeStop {
        @Test
        @DisplayName("range(3) -> [0, 1, 2]")
        void testWithEndBiggerThanStart() {
            int end = 3;
            TestSubject<List<Integer>> result = new TestSubject<>();
            List<Integer> expectedList = Arrays.asList(0, 1, 2);

            TestCase.assumeThat(end, TestStep.GreaterThan(0))

                    .testStep(() -> result.setSubject(LoopUtils.range(end)
                            .boxed()
                            .collect(Collectors.toList()))
                    )

                    .resultingInSubject(result, TestStep.Equals(expectedList));
        }

        @Test
        @DisplayName("range(0) -> []")
        void testWithEmptySlice() {
            int end = 0;
            TestSubject<List<Integer>> result = new TestSubject<>();

            TestCase.assumeThat(end, TestStep.Equals(0))

                    .testStep(() -> result.setSubject(LoopUtils.range(end)
                            .boxed()
                            .collect(Collectors.toList()))
                    )

                    .resultingInSubject(result, TestStep.Fulfills(List::isEmpty));
        }
    }

    @Nested
    @DisplayName("Test range(start, stop)")
    class TestRangeStartStop {
        @Test
        @DisplayName("range(0, 3) -> [0, 1, 2]")
        void testWithEndBiggerThanStart() {
            int start = 0;
            int end = 3;
            TestSubject<List<Integer>> result = new TestSubject<>();
            List<Integer> expectedList = Arrays.asList(0, 1, 2);

            TestCase.assumeThat(start, TestStep.LessThan(end))

                    .testStep(() -> result.setSubject(LoopUtils.range(start, end)
                            .boxed()
                            .collect(Collectors.toList()))
                    )

                    .resultingInSubject(result, TestStep.Equals(expectedList));
        }

        @Test
        @DisplayName("range(2, -1) -> []")
        void testWithEndLowerThanStart() {
            int start = 2;
            int end = -1;
            TestSubject<List<Integer>> result = new TestSubject<>();

            TestCase.assumeThat(end, TestStep.LessThan(start))

                    .testStep(() -> result.setSubject(LoopUtils.range(start, end)
                            .boxed()
                            .collect(Collectors.toList()))
                    )

                    .resultingInSubject(result, TestStep.Fulfills(List::isEmpty));
        }

        @Test
        @DisplayName("range(0, 0) -> []")
        void testWithEmptySlice() {
            int start = 0;
            int end = 0;
            TestSubject<List<Integer>> result = new TestSubject<>();

            TestCase.assumeThat(end, TestStep.Equals(start))

                    .testStep(() -> result.setSubject(LoopUtils.range(start, end)
                            .boxed()
                            .collect(Collectors.toList()))
                    )

                    .resultingInSubject(result, TestStep.Fulfills(List::isEmpty));
        }
    }

    @Nested
    @DisplayName("Test range(start, stop, step)")
    class TestRangeStartStopStep {
        @Test
        @DisplayName("range(0, 3, 1) -> [0, 1, 2]")
        void testWithEndBiggerThanStartPositiveStep() {
            int start = 0;
            int end = 3;
            int step = 1;
            TestSubject<List<Integer>> result = new TestSubject<>();
            List<Integer> expectedList = Arrays.asList(0, 1, 2);

            TestCase.assumeThat(start, TestStep.LessThan(end))
                    .assumeThat(step, TestStep.GreaterThan(0))

                    .testStep(() -> result.setSubject(LoopUtils.range(start, end, step)
                            .boxed()
                            .collect(Collectors.toList()))
                    )

                    .resultingInSubject(result, TestStep.Equals(expectedList));
        }

        @Test
        @DisplayName("range(0, 2, -1) -> []")
        void testWithEndBiggerThanStartNegativeStep() {
            int start = 0;
            int end = 2;
            int step = -1;
            TestSubject<List<Integer>> result = new TestSubject<>();

            TestCase.assumeThat(start, TestStep.LessThan(end))
                    .assumeThat(step, TestStep.LessThan(0))

                    .testStep(() -> result.setSubject(LoopUtils.range(start, end, step)
                            .boxed()
                            .collect(Collectors.toList()))
                    )

                    .resultingInSubject(result, TestStep.Fulfills(List::isEmpty));
        }

        @Test
        @DisplayName("range(2, -1, 1) -> []")
        void testWithEndLowerThanStartPositiveStep() {
            int start = 2;
            int end = -1;
            int step = 1;
            TestSubject<List<Integer>> result = new TestSubject<>();

            TestCase.assumeThat(end, TestStep.LessThan(start))
                    .assumeThat(step, TestStep.GreaterThan(0))

                    .testStep(() -> result.setSubject(LoopUtils.range(start, end, step)
                            .boxed()
                            .collect(Collectors.toList()))
                    )

                    .resultingInSubject(result, TestStep.Fulfills(List::isEmpty));
        }

        @Test
        @DisplayName("range(2, -1, -1) -> [2, 1, 0]")
        void testWithEndLowerThanStartNegativeStep() {
            int start = 2;
            int end = -1;
            int step = -1;
            TestSubject<List<Integer>> result = new TestSubject<>();
            List<Integer> expectedList = Arrays.asList(2, 1, 0);

            TestCase.assumeThat(end, TestStep.LessThan(start))
                    .assumeThat(step, TestStep.LessThan(0))

                    .testStep(() -> result.setSubject(LoopUtils.range(start, end, step)
                            .boxed()
                            .collect(Collectors.toList()))
                    )

                    .resultingInSubject(result, TestStep.Equals(expectedList));
        }

        @Test
        @DisplayName("range(0, 0, 1) -> []")
        void testWithEmptySliceAndPositiveStep() {
            int start = 0;
            int end = 0;
            int step = 1;
            TestSubject<List<Integer>> result = new TestSubject<>();

            TestCase.assumeThat(end, TestStep.Equals(start))
                    .assumeThat(step, TestStep.GreaterThan(0))

                    .testStep(() -> result.setSubject(LoopUtils.range(start, end, step)
                            .boxed()
                            .collect(Collectors.toList()))
                    )

                    .resultingInSubject(result, TestStep.Fulfills(List::isEmpty));
        }

        @Test
        @DisplayName("range(0, 0, -1) -> []")
        void testWithEmptySliceAndNegativeStep() {
            int start = 0;
            int end = 0;
            int step = 1;
            TestSubject<List<Integer>> result = new TestSubject<>();

            TestCase.assumeThat(end, TestStep.Equals(start))
                    .assumeThat(step, TestStep.GreaterThan(0))

                    .testStep(() -> result.setSubject(LoopUtils.range(start, end, step)
                            .boxed()
                            .collect(Collectors.toList()))
                    )

                    .resultingInSubject(result, TestStep.Fulfills(List::isEmpty));
        }

        @Test
        @DisplayName("range(0, 3, 0) -> Error")
        void testWithZeroStep() {
            int start = 0;
            int end = 3;
            int step = 0;

            TestCase.assumeThat(end, TestStep.GreaterThan(start))
                    .assumeThat(step, TestStep.Equals(0))

                    .testStep(() -> LoopUtils.range(start, end, step)
                            , Expect.Expecting(IllegalArgumentException.class, "'step' must not be zero!")
                    );
        }
    }
}
