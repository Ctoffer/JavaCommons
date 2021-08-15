package de.ctoffer.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class Backtracking<T> {
    public static enum SolutionState {
        INVALID_STEP, VALID_STEP, SOLUTION
    }

    public T solve(final T initialState) {
        List<List<T>> options = new ArrayList<>();
        options.add(createNewPossibilities(initialState));
        goDown();

        if (solve(0, options)) {
            return options.get(options.size() - 1).get(0);
        } else {
            throw new RuntimeException("No valid solution found");
        }
    }

    protected boolean solve(int level, final List<List<T>> options) {
        List<T> possibilities = options.get(level);
        boolean result = false;

        for (final T possibility : possibilities) {
            System.out.println(possibility);
            var state = checkValidity(possibility);
            if (state == SolutionState.VALID_STEP) {
                List<T> newPossibilities = createNewPossibilities(possibility);
                options.add(newPossibilities);

                goDown();
                boolean solved = solve(level + 1, options);

                if (solved) {
                    result = true;
                    break;
                } else {
                    goUp();
                    options.remove(options.size() - 1);
                }
            } else if (state == SolutionState.SOLUTION) {
                result = true;
                break;
            }
        }

        return result;
    }


    protected abstract SolutionState checkValidity(final T possibility);

    protected abstract List<T> createNewPossibilities(final T possibility);

    protected void goDown() {

    }


    protected void goUp() {

    }
}
