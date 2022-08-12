package de.ctoffer.commons.test.util;

import org.junit.jupiter.api.Assertions;

import java.util.function.Supplier;

public class TestCase {

    public static Precondition assumeThat(boolean condition, Condition evaluation) {
        return new Precondition().assumeThat(condition, evaluation);
    }

    public static Precondition assumeThat(Runnable assertion) {
        return new Precondition().assumeThat(assertion);
    }

    public static <T> Precondition assumeThat(final T object, Comparison<T> comparison) {
        return new Precondition().assumeThat(object, comparison);
    }

    public static class Precondition {
        private Precondition() {

        }

        public Precondition assumeThat(boolean condition, Condition evaluation) {
            evaluation.check(condition);
            return this;
        }

        public Precondition assumeThat(Runnable assertion) {
            assertion.run();
            return this;
        }

        public <T> Precondition assumeThat(final T object, Comparison<T> comparison) {
            comparison.withObject(object);
            return this;
        }

        @SafeVarargs
        public final <T> Precondition assumeThat(final T object, Comparison<T>... comparisons) {
            for (Comparison<T> comparison : comparisons) {
                comparison.withObject(object);
            }
            return this;
        }

        public TestCode testStep(Runnable test) {
            return new TestCode().testStep(test);
        }

        public <X> TestCode testStep(TestSubject<X> subject, Supplier<X> test) {
            return new TestCode().testStep(subject, test);
        }

        public TestCode testStep(Runnable test, Expect expecting) {
            return new TestCode().testStep(test, expecting);
        }
    }

    public static class TestCode {
        private TestCode() {

        }

        public <X> TestCode testStep(TestSubject<X> subject, Supplier<X> test) {
            return testStep(() -> subject.setSubject(test.get()));
        }

        public TestCode testStep(Runnable test) {
            test.run();
            return this;
        }

        public TestCode testStep(Runnable test, Expect expecting) {
            Exception thrown = Assertions.assertThrows(expecting.getExceptionClass(), test::run);
            Assertions.assertEquals(thrown.getMessage(), expecting.getExceptionMessage());
            return this;
        }

        public PostCondition resultingIn(boolean condition, Condition evaluation) {
            return new PostCondition().resultingIn(condition, evaluation);
        }

        public PostCondition resultingIn(Runnable assertion) {
            return new PostCondition().resultingIn(assertion);
        }

        @SafeVarargs
        public final <T> PostCondition resultingIn(final T object, Comparison<T>... comparisons) {
            return new PostCondition().resultingIn(object, comparisons);
        }

        public <T> PostCondition resultingInSubject(final TestSubject<T> testSubject, final Comparison<T> comparison) {
            return resultingIn(testSubject.getSubject(), comparison);
        }

        @SafeVarargs
        public final <T> PostCondition resultingInSubject(final TestSubject<T> testSubject, final Comparison<T>... comparisons) {
            return resultingIn(testSubject.getSubject(), comparisons);
        }
    }

    public static class PostCondition {
        private PostCondition() {

        }

        public PostCondition resultingIn(boolean condition, Condition evaluation) {
            evaluation.check(condition);
            return this;
        }

        public PostCondition resultingIn(Runnable assertion) {
            assertion.run();
            return this;
        }

        public <T> PostCondition resultingIn(final T object, Comparison<T> comparison) {
            comparison.withObject(object);
            return this;
        }

        @SafeVarargs
        public final <T> PostCondition resultingIn(final T object, Comparison<T>... comparisons) {
            for (Comparison<T> comparison : comparisons) {
                comparison.withObject(object);
            }
            return this;
        }

        public <T> PostCondition resultingInSubject(final TestSubject<T> testSubject, final Comparison<T> comparison) {
            return resultingIn(testSubject.getSubject(), comparison);
        }

        @SafeVarargs
        public final <T> PostCondition resultingInSubject(final TestSubject<T> testSubject, final Comparison<T>... comparisons) {
            return resultingIn(testSubject.getSubject(), comparisons);
        }
    }
}

