package de.ctoffer.commons.algorithms.backtracking.sudoku;


import de.ctoffer.commons.algorithms.backtracking.Backtracking;
import de.ctoffer.commons.algorithms.backtracking.InplaceBacktracking;
import de.ctoffer.commons.container.IntPair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

public class InplaceSudokuSolver extends InplaceBacktracking<SudokuField, SudokuMutator, SudokuMutation> {
    private final List<IntPair> openFields = new ArrayList<>();
    private int position = -1;

    public InplaceSudokuSolver() {
        super(new SudokuMutator());

    }

    @Override
    public SudokuField solve(SudokuField initialState) {
        initialState.horizontalCoordIterator().forEachRemaining(entry -> {
            var coordinate = entry.getFirst();
            var value = entry.getSecond();
            if (value == 0) {
                openFields.add(coordinate);
            }
        });

        return super.solve(initialState);
    }

    @Override
    protected void moveForward() {
        ++position;
    }

    @Override
    protected void backtrack() {
        --position;
    }


    @Override
    protected Backtracking.SolutionState checkValidity(SudokuField currentState) {
        var coordinate = openFields.get(position);
        int y = coordinate.getFirst();
        int x = coordinate.getSecond();

        boolean validity = checkColumn(currentState, y) && checkRow(currentState, x) && checkSquare(currentState, y, x);

        final Backtracking.SolutionState result;
        if (validity) {
            result = position == openFields.size() - 1 ? Backtracking.SolutionState.SOLUTION : Backtracking.SolutionState.VALID_STEP;
        } else {
            result = Backtracking.SolutionState.INVALID_STEP;
        }

        return result;
    }

    private boolean checkColumn(final SudokuField field, int column) {
        return checkColumnOrRow(field, column, IntPair::new);
    }

    private boolean checkColumnOrRow(final SudokuField field, int constant, BiFunction<Integer, Integer, IntPair> accessor) {
        int nonEmptyValueCounter = 0;
        Set<Integer> numbers = new HashSet<>();

        for (int var = 0; var < 9; ++var) {
            int value = field.get(accessor.apply(constant, var));

            if (value != 0) {
                ++nonEmptyValueCounter;
                numbers.add(value);
            }
        }

        return numbers.size() == nonEmptyValueCounter;
    }

    private boolean checkRow(final SudokuField field, int row) {
        return checkColumnOrRow(field, row, (constant, var) -> new IntPair(var, constant));
    }

    private boolean checkSquare(final SudokuField field, int y, int x) {
        int nonEmptyValueCounter = 0;
        Set<Integer> numbers = new HashSet<>();

        int topLeftY = 3 * (y / 3);
        int topLeftX = 3 * (x / 3);

        for (int dy = 0; dy < 3; ++dy) {
            for (int dx = 0; dx < 3; ++dx) {
                int value = field.get(topLeftY + dy, topLeftX + dx);
                if (value != 0) {
                    ++nonEmptyValueCounter;
                    numbers.add(value);
                }
            }
        }

        return numbers.size() == nonEmptyValueCounter;
    }
}
