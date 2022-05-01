package de.ctoffer.commons.algorithms.memento;


import java.util.Objects;
import java.util.Optional;

public class UnmodifiableModification <T> implements ModificationBase <T> {

    private Modification<T> instance;

    public UnmodifiableModification(Modification<T> instance) {
        this.instance = Objects.requireNonNull(instance);
    }

    @Override
    public Optional<String> getName() {
        return instance.getName();
    }

    @Override
    public Optional<T> getCurrentSavedState() {
        return instance.getCurrentSavedState();
    }

    @Override
    public boolean wasAlreadyUndone() {
        return instance.wasAlreadyUndone();
    }

    @Override
    public boolean wasApplied() {
        return instance.wasApplied();
    }


    @Override
    public void setName(String name) {
        throw new UnsupportedOperationException("No write access: setName is not allowed");
    }

    @Override
    public void undo() {
        throw new UnsupportedOperationException("No write access: undo is not allowed");
    }

    @Override
    public void redo() {
        throw new UnsupportedOperationException("No write access: redo is not allowed");
    }

    @Override
    public String toString() {
        return String.format("Read-Only{%s}", instance);
    }

    @Override
    public boolean equals(Object obj) {
        // TODO make better comparison
        return toString().equals("" + obj);
    }
}
