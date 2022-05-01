package de.ctoffer.commons.algorithms.backtracking.sudoku;

import de.ctoffer.commons.algorithms.backtracking.InplaceMutator;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SudokuMutator implements InplaceMutator<SudokuField, SudokuMutation> {
    @Override
    public List<SudokuMutation> allMutationsOf(SudokuField object) {
        var maybeCoordinate = object.firstEmptyField();

        if (maybeCoordinate.isPresent()) {
            var coordinate = maybeCoordinate.get();

            Set<Integer> usedNumbers = object.getRow(coordinate);
            usedNumbers.addAll(object.getColumn(coordinate));
            usedNumbers.addAll(object.getSquare(coordinate));

            return IntStream.rangeClosed(1, 9)
                    .filter(i -> !(usedNumbers.contains(i)))
                    .mapToObj(value -> new SudokuMutation(coordinate, value))
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }
}
