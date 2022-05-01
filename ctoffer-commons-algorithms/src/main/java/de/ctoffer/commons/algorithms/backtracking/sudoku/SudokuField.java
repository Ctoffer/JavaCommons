package de.ctoffer.commons.algorithms.backtracking.sudoku;

import de.ctoffer.commons.algorithms.backtracking.Copyable;
import de.ctoffer.commons.container.Int2DArray;
import de.ctoffer.commons.container.IntPair;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SudokuField extends Int2DArray implements Copyable<SudokuField> {
    public SudokuField() {
        super(9, 9);
    }

    public SudokuField copy() {
        var array = new SudokuField();
        System.arraycopy(this.buffer, 0, array.buffer, 0, this.size());

        return array;
    }

    public static SudokuField initializeSimple() {
        final var field = new SudokuField();

        field.setRow(0, 5, 3, 0, 0, 7, 0, 0, 0, 0);
        field.setRow(1, 6, 0, 0, 1, 9, 5, 0, 0, 0);
        field.setRow(2, 0, 9, 8, 0, 0, 0, 0, 6, 0);

        field.setRow(3, 8, 0, 0, 0, 6, 0, 0, 0, 3);
        field.setRow(4, 4, 0, 0, 8, 0, 3, 0, 0, 1);
        field.setRow(5, 7, 0, 0, 0, 2, 0, 0, 0, 6);

        field.setRow(6, 0, 6, 0, 0, 0, 0, 2, 8, 0);
        field.setRow(7, 0, 0, 0, 4, 1, 9, 0, 0, 5);
        field.setRow(8, 0, 0, 0, 0, 8, 0, 0, 7, 9);

        System.out.println(field);

        return field;
    }

    public static SudokuField nearlySolved() {
        final var field = new SudokuField();

        field.setRow(0, 5, 3, 4, 6, 7, 8, 9, 1, 2);
        field.setRow(1, 6, 7, 2, 1, 9, 5, 3, 4, 8);
        field.setRow(2, 1, 9, 8, 3, 4, 2, 5, 6, 7);

        field.setRow(3, 8, 5, 9, 7, 6, 1, 4, 2, 3);
        field.setRow(4, 4, 2, 6, 8, 5, 3, 7, 9, 1);
        field.setRow(5, 7, 1, 3, 9, 2, 4, 8, 5, 6);

        field.setRow(6, 9, 6, 1, 5, 3, 7, 2, 8, 0);
        field.setRow(7, 2, 8, 7, 4, 1, 9, 0, 0, 5);
        field.setRow(8, 3, 4, 5, 2, 8, 6, 0, 7, 9);

        System.out.println(field);

        return field;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (int y = 0; y < 9; ++y) {
            for (int x = 0; x < 9; ++x) {
                int value = get(y, x);
                builder.append(value).append(" ");
                if ((x + 1) % 3 == 0 && x != 8) {
                    builder.append("| ");
                }
            }
            builder.append("\n");
            if ((y + 1) % 3 == 0 && y != 8) {
                builder.append("------+-------+-------\n");
            }
        }

        return builder.toString();
    }

    public static void main(String[] args) {
        var field = SudokuField.initializeSimple();
        var solution = new InplaceSudokuSolver().solve(field);
        System.out.println("======== SOLUTION =========");
        System.out.println(solution);

    }

    public Optional<IntPair> firstEmptyField() {
        int position = -1;
        for (int i = 0; i < size(); ++i) {
            if (this.buffer[i] == 0) {
                position = i;
                break;
            }
        }

        int y = position / getWidth();
        int x = position % getWidth();

        return position == -1 ? Optional.empty() : Optional.of(new IntPair(y, x));
    }

    public Set<Integer> getRow(IntPair coordinate) {
        return IntStream.rangeClosed(0, 8)
                .mapToObj(y -> new IntPair(y, coordinate.second))
                .map(this::get)
                .filter(i -> i != 0)
                .collect(Collectors.toSet());
    }

    public Set<Integer> getColumn(IntPair coordinate) {
        return IntStream.rangeClosed(0, 8)
                .mapToObj(x -> new IntPair(coordinate.first, x))
                .map(this::get)
                .filter(i -> i != 0)
                .collect(Collectors.toSet());
    }

    public Set<Integer> getSquare(IntPair coordinate) {
        final Set<Integer> result = new HashSet<>();
        int topLeftY = 3 * (coordinate.first / 3);
        int topLeftX = 3 * (coordinate.second / 3);

        for (int dy = 0; dy < 3; ++dy) {
            for (int dx = 0; dx < 3; ++dx) {
                var value = get(topLeftY + dy, topLeftX + dx);
                if (value != 0) {
                    result.add(value);
                }
            }
        }

        return result;
    }
}
