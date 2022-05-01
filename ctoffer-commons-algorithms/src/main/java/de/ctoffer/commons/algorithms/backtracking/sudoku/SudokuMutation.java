package de.ctoffer.commons.algorithms.backtracking.sudoku;

import de.ctoffer.commons.algorithms.backtracking.InplaceMutation;
import de.ctoffer.commons.container.IntPair;

public class SudokuMutation implements InplaceMutation<SudokuField> {
    private final IntPair position;
    private final int value;

    public SudokuMutation(final IntPair position, int value) {
        this.position = position;
        this.value = value;
    }

    @Override
    public void activate(SudokuField object) {
        object.set(position, value);
    }

    @Override
    public void deactivate(SudokuField object) {
        object.set(position, 0);
    }
}
