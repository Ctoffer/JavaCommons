package de.ctoffer.algorithms;

import java.util.ArrayList;
import java.util.List;

public abstract class InplaceBacktracking<T extends Copyable<T>, M extends InplaceMutator<T, N>, N extends InplaceMutation<T>> {
    private final M mutator;

    public InplaceBacktracking(final M mutator) {
        this.mutator = mutator;
    }

    public T solve(final T initialState) {
        T workingCopy = initialState.copy();
        List<N> mutations = new ArrayList<>();

        boolean solved = exploreMutations(0, workingCopy, mutations);

        if (!solved) {
            throw new RuntimeException("No solution found");
        }

        return workingCopy;
    }

    private boolean exploreMutations(int level, T currentState, List<N> mutations) {
        boolean result = false;
        for (final N mutation : mutator.allMutationsOf(currentState)) {
            mutation.activate(currentState);
            mutations.add(mutation);
            moveForward();

            boolean solved = solve(level + 1, currentState, mutations);

            if (solved) {
                result = true;
                break;
            }

            backtrack();
            mutation.deactivate(currentState);
            mutations.remove(mutation);
        }

        return result;
    }

    protected abstract void moveForward();
    protected abstract void backtrack();

    private boolean solve(int level, T currentState, List<N> mutations) {
        boolean result = false;
        var state = checkValidity(currentState);

        if (state == Backtracking.SolutionState.VALID_STEP) {
            result = exploreMutations(level, currentState, mutations);
        } else if (state == Backtracking.SolutionState.SOLUTION) {
            result = true;
        }

        return result;
    }

    protected abstract Backtracking.SolutionState checkValidity(T currentState);
}
