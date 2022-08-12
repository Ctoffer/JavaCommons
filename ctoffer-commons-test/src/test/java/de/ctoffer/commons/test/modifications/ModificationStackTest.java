package de.ctoffer.commons.test.modifications;

import de.ctoffer.commons.test.util.TestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static de.ctoffer.commons.test.util.Expect.Expecting;
import static de.ctoffer.commons.test.util.TestStep.*;

class ModificationStackTest {
    private ModificationStack uninitializedModifications;
    private ModificationStack modifications;
    private ModificationDataMock<Integer> integerData1;
    private Modification<Integer> integerModification1;

    @BeforeEach
    void setup() {
        uninitializedModifications = null;
        modifications = new ModificationStack();
        integerData1 = new ModificationDataMock<>(0);
        integerModification1 = modification(integerData1);
        integerData1.setData(randInt());
    }

    private <T> Modification<T> modification(final String name, final ModificationDataMock<T> mock) {
        Modification<T> mod = modification(mock);
        mod.setName(name);
        return mod;
    }

    private <T> Modification<T> modification(final ModificationDataMock<T> mock) {
        return new Modification<>(mock::getData, mock::setData);
    }

    private int randInt() {
        return (int) (1000 * Math.random());
    }

    @Test
    void testConstructorNoArguments() {
        TestCase.assumeThat(uninitializedModifications, IsNull())

                .testStep(() -> setUninitializedModifications(new ModificationStack()))

                .resultingIn(uninitializedModifications, IsNotNull())
                .resultingIn(uninitializedModifications.getLimit(), Equals(Integer.MAX_VALUE));
    }

    private void setUninitializedModifications(ModificationStack newStack) {
        this.uninitializedModifications = newStack;
    }

    @Test
    void testConstructorWithLimit() {
        int testLimit = 5;
        TestCase.assumeThat(uninitializedModifications, IsNull())

                .testStep(() -> setUninitializedModifications(new ModificationStack(testLimit)))

                .resultingIn(uninitializedModifications, IsNotNull())
                .resultingIn(uninitializedModifications.getLimit(), Equals(testLimit));
    }

    @Test
    void testConstructorWithNegativeLimit() {
        int testLimit = -5;
        TestCase.assumeThat(uninitializedModifications, IsNull())

                .testStep(() -> setUninitializedModifications(new ModificationStack(testLimit))
                        , Expecting(IllegalArgumentException.class, "Limit must be greater than 0")
                );

    }

    @Test
    void testConstructorWithZeroLimit() {
        int testLimit = 0;
        TestCase.assumeThat(uninitializedModifications, IsNull())

                .testStep(() -> setUninitializedModifications(new ModificationStack(testLimit))
                        , Expecting(IllegalArgumentException.class, "Limit must be greater than 0")
                );
    }

    @Test
    void testRememberWithNull() {
        TestCase.assumeThat(modifications, IsNotNull())

                .testStep(() -> modifications.remember(null),
                        Expecting(IllegalArgumentException.class, "Given modification must be non-null.")
                );
    }

    @Test
    void testRememberWithUndoneModification() {
        integerModification1.undo();
        TestCase.assumeThat(modifications, IsNotNull())
                .assumeThat(integerModification1.wasAlreadyUndone(), IS_TRUE)

                .testStep(() -> modifications.remember(integerModification1),
                        Expecting(IllegalArgumentException.class,
                                "Can only save modifications, which are not already undone."
                        )
                );
    }

    @Test
    void testRemember() {
        TestCase.assumeThat(modifications.getLimit(), Equals(Integer.MAX_VALUE))
                .assumeThat(modifications.hasNoUndoModifications(), IS_TRUE)
                .assumeThat(modifications.hasNoRedoModifications(), IS_TRUE)
                .assumeThat(modifications.getUndoableModifications(), Fulfills(List::isEmpty))
                .assumeThat(modifications.getRedoableModifications(), Fulfills(List::isEmpty))

                .testStep(() -> modifications.remember(integerModification1))

                .resultingIn(modifications.hasNoUndoModifications(), IS_FALSE)
                .resultingIn(modifications.hasNoRedoModifications(), IS_TRUE)
                .resultingIn(modifications.getUndoableModifications(),
                        FulfillsNot(List::isEmpty),
                        Where(list -> list.get(0), Equals(integerModification1.unmodifiable()))
                )
                .resultingIn(modifications.getRedoableModifications(), Fulfills(List::isEmpty));
    }

    @Test
    void testRememberAutoRemoveOfRedoableModifications() {
        Modification<Integer> secondModification = modification("2nd Modification", integerData1);
        modifications.remember(integerModification1);
        modifications.undoLastModification();

        TestCase.assumeThat(modifications.hasNoUndoModifications(), IS_TRUE)
                .assumeThat(modifications.hasNoRedoModifications(), IS_FALSE)
                .assumeThat(modifications.size(), Equals(1))
                .assumeThat(modifications.getUndoableModifications(), Fulfills(List::isEmpty))
                .assumeThat(modifications.getRedoableModifications(),
                        FulfillsNot(List::isEmpty),
                        Where(list -> list.get(0), Equals(integerModification1.unmodifiable()))
                )

                .testStep(() -> modifications.remember(secondModification))

                .resultingIn(modifications.hasNoUndoModifications(), IS_FALSE)
                .resultingIn(modifications.hasNoRedoModifications(), IS_TRUE)
                .resultingIn(modifications.size(), Equals(1))
                .resultingIn(modifications.getUndoableModifications(),
                        FulfillsNot(List::isEmpty),
                        Where(list -> list.get(0), Equals(secondModification.unmodifiable()))
                )
                .resultingIn(modifications.getRedoableModifications(), Fulfills(List::isEmpty));
    }

    @Test
    void testRememberLimitReached() {
        ModificationStack stack = new ModificationStack(1);
        stack.remember(integerModification1);
        Modification<Integer> secondModification = modification("2nd Modification", integerData1);

        TestCase.assumeThat(stack.getLimit(), Equals(1))
                .assumeThat(stack.hasNoUndoModifications(), IS_FALSE)
                .assumeThat(stack.hasNoRedoModifications(), IS_TRUE)
                .assumeThat(stack.size(), Equals(1))
                .assumeThat(stack.getUndoableModifications(),
                        FulfillsNot(List::isEmpty),
                        Where(list -> list.get(0), Equals(integerModification1.unmodifiable()))
                )
                .assumeThat(stack.getRedoableModifications(), Fulfills(List::isEmpty))

                .testStep(() -> stack.remember(secondModification))

                .resultingIn(stack.getLimit(), Equals(1))
                .resultingIn(stack.hasNoUndoModifications(), IS_FALSE)
                .resultingIn(stack.hasNoRedoModifications(), IS_TRUE)
                .resultingIn(stack.size(), Equals(1))
                .resultingIn(stack.getUndoableModifications(),
                        FulfillsNot(List::isEmpty),
                        Where(list -> list.get(0), Equals(secondModification.unmodifiable()))
                )
                .resultingIn(stack.getRedoableModifications(), Fulfills(List::isEmpty));
    }

    @Test
    void testUndo() {
        modifications.remember(integerModification1);

        TestCase.assumeThat(modifications.hasNoUndoModifications(), IS_FALSE)
                .assumeThat(modifications.hasNoRedoModifications(), IS_TRUE)
                .assumeThat(modifications.size(), Equals(1))
                .assumeThat(modifications.getUndoableModifications(),
                        FulfillsNot(List::isEmpty),
                        Where(list -> list.get(0), Equals(integerModification1.unmodifiable()))
                )
                .assumeThat(modifications.getRedoableModifications(), Fulfills(List::isEmpty))

                .testStep(() -> modifications.undoLastModification())

                .resultingIn(modifications.hasNoUndoModifications(), IS_TRUE)
                .resultingIn(modifications.hasNoRedoModifications(), IS_FALSE)
                .resultingIn(modifications.size(), Equals(1))
                .resultingIn(modifications.getUndoableModifications(), Fulfills(List::isEmpty))
                .resultingIn(modifications.getRedoableModifications(),
                        FulfillsNot(List::isEmpty),
                        Where(list -> list.get(0), Equals(integerModification1.unmodifiable()))
                );
    }

    @Test
    void testUndoWithoutUndoableModifications() {
        TestCase.assumeThat(modifications.hasNoUndoModifications(), IS_TRUE)
                .assumeThat(modifications::isEmpty)
                .assumeThat(modifications.getUndoableModifications(), Fulfills(List::isEmpty))

                .testStep(() -> modifications.undoLastModification(), Expecting(
                        IllegalStateException.class,
                        "There is nothing to undo.")
                );
    }

    @Test
    void testRedo() {
        modifications.remember(integerModification1);
        modifications.undoLastModification();

        TestCase.assumeThat(modifications.hasNoUndoModifications(), IS_TRUE)
                .assumeThat(modifications.hasNoRedoModifications(), IS_FALSE)
                .assumeThat(modifications.size(), Equals(1))
                .assumeThat(modifications.getUndoableModifications(), Fulfills(List::isEmpty))
                .assumeThat(modifications.getRedoableModifications(),
                        FulfillsNot(List::isEmpty),
                        Where(list -> list.get(0), Equals(integerModification1.unmodifiable()))
                )
                .testStep(() -> modifications.redoLastModification())

                .resultingIn(modifications.hasNoUndoModifications(), IS_FALSE)
                .resultingIn(modifications.hasNoRedoModifications(), IS_TRUE)
                .resultingIn(modifications.size(), Equals(1))
                .resultingIn(modifications.getUndoableModifications(),
                        FulfillsNot(List::isEmpty),
                        Where(list -> list.get(0), Equals(integerModification1.unmodifiable()))
                )
                .resultingIn(modifications.getRedoableModifications(), Fulfills(List::isEmpty));
    }

    @Test
    void testRedoWithoutRedoableModifications() {
        TestCase.assumeThat(modifications.hasNoRedoModifications(), IS_TRUE)
                .assumeThat(modifications::isEmpty)
                .assumeThat(modifications.getRedoableModifications(), Fulfills(List::isEmpty))

                .testStep(() -> modifications.redoLastModification(), Expecting(
                        IllegalStateException.class,
                        "There is nothing to redo.")
                );
    }
}
