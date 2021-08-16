package de.ctoffer.algorithms.sudoku;

import de.ctoffer.algorithms.InplaceMutator;
import de.ctoffer.utils.IntPair;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SudokuMutator implements InplaceMutator<SudokuField, SudokuMutation> {
    @Override
    public List<SudokuMutation> allMutationsOf(SudokuField object) {
        var maybeCoordinate = object.firstEmptyField();

        if (maybeCoordinate.isPresent()) {
            var coordinate = maybeCoordinate.get();
            return IntStream.rangeClosed(1, 9)
                    .mapToObj(value -> new SudokuMutation(coordinate, value))
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }
}
