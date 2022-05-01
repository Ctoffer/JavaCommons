package de.ctoffer.commons.algorithms.memento;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import static java.util.stream.Collectors.toUnmodifiableList;

public class ModificationStack {

    public static final int UNLIMITED = Integer.MAX_VALUE;

    private final int limit;
    private final Deque<Modification<?>> undoableModifications;
    private final Deque<Modification<?>> redoableModifications;

    public ModificationStack() {
        this(UNLIMITED);
    }

    public ModificationStack(int limit) {
        if(limit <= 0) {
            throw new IllegalArgumentException("Limit must be greater than 0");
        }

        this.limit = limit;
        this.undoableModifications = new ArrayDeque<>();
        this.redoableModifications = new ArrayDeque<>();
    }

    public void remember(final Modification<?> modification) {
        if(modification == null) {
            throw new IllegalArgumentException("Given modification must be non-null.");
        } else if(modification.wasAlreadyUndone()) {
            throw new IllegalArgumentException("Can only save modifications, which are not already undone.");
        }

        redoableModifications.clear();
        if(reachedLimit()) {
            undoableModifications.removeFirst();
        }
        undoableModifications.push(modification);
    }

    private boolean reachedLimit() {
        return size() == limit;
    }

    public void undoLastModification() {
        if (hasNoUndoModifications()) {
            throw new IllegalStateException("There is nothing to undo.");
        }

        Modification<?> mod = undoableModifications.pop();
        mod.undo();
        redoableModifications.push(mod);
    }

    public boolean hasNoUndoModifications() {
        return undoableModifications.isEmpty();
    }

    public void redoLastModification() {
        if(hasNoRedoModifications()) {
            throw new IllegalStateException("There is nothing to redo.");
        }

        Modification<?> mod = redoableModifications.pop();
        mod.redo();
        undoableModifications.push(mod);
    }

    public boolean hasNoRedoModifications() {
        return redoableModifications.isEmpty();
    }

    public int getLimit() {
        return limit;
    }

    public int size() {
        return undoableModifications.size() + redoableModifications.size();
    }

    public boolean isEmpty() {
        return size() <= 0;
    }

    public List<ModificationBase<?>> getUndoableModifications() {
        return undoableModifications.stream()
                .map(Modification::unmodifiable)
                .collect(toUnmodifiableList());
    }

    public List<ModificationBase<?>> getRedoableModifications() {
        return redoableModifications.stream()
                .map(Modification::unmodifiable)
                .collect(toUnmodifiableList());
    }
}
