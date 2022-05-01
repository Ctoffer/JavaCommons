package de.ctoffer.commons.test.modifications;

import de.ctoffer.commons.test.util.TestCase;
import de.ctoffer.commons.test.util.TestSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static de.ctoffer.commons.test.util.Expect.Expecting;
import static de.ctoffer.commons.test.util.TestStep.*;
import static java.lang.String.format;

class ModificationTest {
    private static final int INITIAL_VALUE = 0;
    private int nextValue;
    private ModificationDataMock<Integer> integerData;
    private Modification<Integer> integerModification;

    @BeforeEach
    void setup() {
        integerData = new ModificationDataMock<>(INITIAL_VALUE);
        integerModification = new Modification<>(integerData::getData, integerData::setData);
        nextValue = (int) (1000 * Math.random());
        integerData.setData(nextValue);
    }

    @Test
    void testEmptyName() {
        final String name = "Random Modification";
        TestCase.assumeThat(integerModification.getName(), FulfillsNot(Optional::isPresent))
                .testStep(() -> integerModification.setName(name))
                .resultingIn(integerModification.getName(),
                        Fulfills(Optional::isPresent),
                        Where(Optional::get, Equals(name))
                );
    }

    @Test
    void testUndo() {
        TestCase.assumeThat(integerModification.wasApplied(), IS_TRUE)
                .assumeThat(integerModification.wasAlreadyUndone(), IS_FALSE)
                .assumeThat(integerModification.getCurrentSavedState(),
                        Fulfills(Optional::isPresent),
                        Where(Optional::get, Equals(INITIAL_VALUE))
                )

                .testStep(integerModification::undo)

                .resultingIn(integerModification.wasApplied(), IS_FALSE)
                .resultingIn(integerModification.wasAlreadyUndone(), IS_TRUE)
                .resultingIn(integerModification.getCurrentSavedState(),
                        Fulfills(Optional::isPresent),
                        Where(Optional::get, Equals(nextValue))
                )
                .resultingIn(integerData.getData(), Equals(INITIAL_VALUE));
    }

    @Test
    void testRedo() {
        integerModification.undo();
        TestCase.assumeThat(integerModification.wasApplied(), IS_FALSE)
                .assumeThat(integerModification.wasAlreadyUndone(), IS_TRUE)
                .assumeThat(integerModification.getCurrentSavedState(),
                        Fulfills(Optional::isPresent),
                        Where(Optional::get, Equals(nextValue))
                )

                .testStep(integerModification::redo)

                .resultingIn(integerModification.wasApplied(), IS_TRUE)
                .resultingIn(integerModification.wasAlreadyUndone(), IS_FALSE)
                .resultingIn(integerModification.getCurrentSavedState(),
                        Fulfills(Optional::isPresent),
                        Where(Optional::get, Equals(INITIAL_VALUE))
                )
                .resultingIn(integerData.getData(), Equals(nextValue));
    }

    @Test
    void testUndoIfAlreadyUndone() {
        final String exceptionMessage = "This modification was already redone and cannot be redone twice in a row.";
        TestCase.assumeThat(integerModification.wasApplied(), IS_TRUE)
                .assumeThat(integerModification.wasAlreadyUndone(), IS_FALSE)
                .assumeThat(integerModification.getCurrentSavedState(),
                        Fulfills(Optional::isPresent),
                        Where(Optional::get, Equals(INITIAL_VALUE))
                )

                .testStep(integerModification::redo, Expecting(IllegalStateException.class, exceptionMessage));
    }

    @Test
    void testRedoIfAlreadyRedone() {
        final String exceptionMessage = "This modification was already undone and cannot be undone twice in a row.";
        integerModification.undo();
        TestCase.assumeThat(integerModification.wasApplied(), IS_FALSE)
                .assumeThat(integerModification.wasAlreadyUndone(), IS_TRUE)
                .assumeThat(integerModification.getCurrentSavedState(),
                        Fulfills(Optional::isPresent),
                        Where(Optional::get, Equals(nextValue))
                )

                .testStep(integerModification::undo, Expecting(IllegalStateException.class, exceptionMessage));
    }

    @Test
    void testToStringWithoutName() {
        TestSubject<String> stringSubject = new TestSubject<>();
        TestCase.assumeThat(integerModification.getName().isPresent(), IS_FALSE)

                .testStep(() -> stringSubject.setSubject(integerModification.toString()))

                .resultingIn(stringSubject.getSubject(), Equals("Unnamed [state={0}]"));
    }

    @Test
    void testToStringWithName() {
        TestSubject<String> stringSubject = new TestSubject<>();
        String name = "Set Number";
        integerModification.setName(name);

        TestCase.assumeThat(integerModification.getName().isPresent(), IS_TRUE)

                .testStep(() -> stringSubject.setSubject(integerModification.toString()))

                .resultingIn(stringSubject.getSubject(), Equals(name + " [state={0}]"));
    }

    @Test
    void testReadOnlyAccess() {
        final Class<UnsupportedOperationException> errorClass = UnsupportedOperationException.class;
        final String errorTemplate = "No write access: %s is not allowed";
        final String name = "Random Modification";
        final ModificationBase<Integer> readOnly = integerModification.unmodifiable();
        integerModification.setName(name);

        TestCase.assumeThat(readOnly, IsNotNull())
                .assumeThat(
                        readOnly.getCurrentSavedState(),
                        Equals(integerModification.getCurrentSavedState())
                )
                .assumeThat(
                        readOnly.getName(),
                        Equals(integerModification.getName())
                )
                .assumeThat(
                        readOnly.wasAlreadyUndone(),
                        Equals(integerModification.wasAlreadyUndone())
                )
                .assumeThat(
                        readOnly.wasApplied(),
                        Equals(integerModification.wasApplied())
                )
                .assumeThat(
                        readOnly.toString(),
                        Equals(format("Read-Only{%s}", integerModification))
                )

                .testStep(() -> readOnly.setName("Other Name"),
                        Expecting(errorClass, format(errorTemplate, "setName"))
                )
                .testStep(readOnly::undo,
                        Expecting(errorClass, format(errorTemplate, "undo"))
                )
                .testStep(readOnly::redo,
                        Expecting(errorClass, format(errorTemplate, "redo"))
                );
    }

}

class ModificationDataMock<T> {
    private T data;

    ModificationDataMock(final T data) {
        this.data = data;
    }

    T getData() {
        return this.data;
    }

    void setData(final T data) {
        this.data = data;
    }
}
