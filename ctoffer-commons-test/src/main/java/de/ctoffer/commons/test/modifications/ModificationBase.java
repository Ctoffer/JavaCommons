package de.ctoffer.commons.test.modifications;

import java.util.Optional;

public interface ModificationBase<T> {
    void setName(final String name);

    Optional<String> getName();

    Optional<T> getCurrentSavedState();

    boolean wasAlreadyUndone();

    boolean wasApplied();

    void undo();

    void redo();
}
