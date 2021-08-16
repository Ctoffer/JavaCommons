package de.ctoffer.algorithms.sudoku;

import de.ctoffer.algorithms.InplaceMutation;
import de.ctoffer.utils.IntPair;

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
