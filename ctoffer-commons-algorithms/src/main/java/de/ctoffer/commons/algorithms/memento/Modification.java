package de.ctoffer.commons.algorithms.memento;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 *
 * @param <T>
 */
public class Modification <T> implements  ModificationBase<T> {

    private final Supplier<T> getState;
    private final Consumer<T> setState;

    private T savedState;
    private String name;
    private boolean wasAlreadyRestored;

    public Modification(final Supplier<T> getState, final Consumer<T> setState) {
        this.getState = getState;
        this.setState = setState;
        this.savedState = getState.get();
        this.wasAlreadyRestored = false;
    }

    @Override
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    @Override
    public Optional<T> getCurrentSavedState() {
        return Optional.ofNullable(savedState);
    }

    @Override
    public boolean wasAlreadyUndone() {
        return wasAlreadyRestored;
    }

    @Override
    public boolean wasApplied() {
        return !wasAlreadyUndone();
    }

    @Override
    public void undo() {
        if(wasAlreadyUndone()) {
            throw new IllegalStateException("This modification was already undone and cannot be undone twice in a row.");
        }

        wasAlreadyRestored = true;
        T oldState = savedState;
        savedState = getState.get();
        setState.accept(oldState);
    }

    @Override
    public void redo() {
        if(wasApplied()) {
            throw new IllegalStateException("This modification was already redone and cannot be redone twice in a row.");
        }

        wasAlreadyRestored = false;
        T newState = savedState;
        savedState = getState.get();
        setState.accept(newState);
    }

    public ModificationBase<T> unmodifiable() {
        return new UnmodifiableModification<>(this);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        if(getName().isPresent()) {
            builder.append(name);
        } else {
            builder.append("Unnamed");
        }

        builder.append(" [state={")
                .append(savedState)
                .append("}]");

        return builder.toString();
    }
}
