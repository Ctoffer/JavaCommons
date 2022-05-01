package de.ctoffer.commons.algorithms.backtracking.sudoku;

import de.ctoffer.commons.algorithms.backtracking.Backtracking;
import de.ctoffer.commons.container.IntPair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SudokuSolver extends Backtracking<SudokuField> {
    private List<IntPair> openFields = new ArrayList<>();
    private int position = -1;

    @Override
    public SudokuField solve(SudokuField initialState) {
        initialState.horizontalCoordIterator().forEachRemaining(entry -> {
            var coordinate = entry.getFirst();
            var value = entry.getSecond();
            if (value == 0) {
                openFields.add(coordinate);
            }
        });

        System.out.println(openFields);

        return super.solve(initialState);
    }

    @Override
    protected SolutionState checkValidity(final SudokuField possibility) {
        var coordinate = openFields.get(position);
        int y = coordinate.getFirst();
        int x = coordinate.getSecond();

        boolean validity = checkColumn(possibility, y) && checkRow(possibility, x) && checkSquare(possibility, y, x);

        final SolutionState result;
        if (validity) {
            result = position == openFields.size() - 1 ? SolutionState.SOLUTION : SolutionState.VALID_STEP;
        } else {
            result = SolutionState.INVALID_STEP;
        }

        return result;
    }

    private boolean checkColumn(final SudokuField field, int column) {
        int nonEmptyValueCounter = 0;
        Set<Integer> numbers = new HashSet<>();

        for (int x = 0; x < 9; ++x) {
            int value = field.get(column, x);
            if (value != 0) {
                ++nonEmptyValueCounter;
                numbers.add(value);
            }
        }

        return numbers.size() == nonEmptyValueCounter;
    }

    private boolean checkRow(final SudokuField field, int row) {
        int nonEmptyValueCounter = 0;
        Set<Integer> numbers = new HashSet<>();

        for (int y = 0; y < 9; ++y) {
            int value = field.get(y, row);
            if (value != 0) {
                ++nonEmptyValueCounter;
                numbers.add(value);
            }
        }

        return numbers.size() == nonEmptyValueCounter;
    }

    private boolean checkSquare(final SudokuField field, int y, int x) {
        int nonEmptyValueCounter = 0;
        Set<Integer> numbers = new HashSet<>();

        int topLeftY = 3 * (y / 3);
        int topLeftX = 3 * (x / 3);

        for(int dy = 0; dy < 3; ++dy) {
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

    @Override
    protected void goDown() {
        ++position;
    }

    @Override
    protected void goUp() {
        --position;
    }

    @Override
    protected List<SudokuField> createNewPossibilities(SudokuField possibility) {
        List<SudokuField> fields = new ArrayList<>();
        var nextPosition = openFields.get(position + 1);
        for (int i = 1; i < 10; ++i) {
            var field = possibility.copy();
            field.set(nextPosition, i);
            fields.add(field);
        }
        return fields;
    }
}
