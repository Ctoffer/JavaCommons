package de.ctoffer.commons.container;

import lombok.Getter;

import java.util.Iterator;

public class Int2DArray {

    @Getter
    private final int height;

    @Getter
    private final int width;

    protected int[] buffer;

    public Int2DArray(final int height, int width) {
        this.height = height;
        this.width = width;
        this.buffer = new int[height * width];
    }

    public int size() {
        return height * width;
    }

    public int get(final IntPair coordinate) {
        return get(coordinate.first, coordinate.second);
    }

    public int get(int y, int x) {
        checkY(y);
        checkX(x);

        return this.buffer[resolveIndex(y, x)];
    }

    private void checkY(int y) {
        if (!(checkRange(y, 0, getHeight()))) {
            throw new ArrayIndexOutOfBoundsException("Y out of bounds!");
        }
    }

    private static boolean checkRange(int value, int min, int max) {
        return min <= value && value < max;
    }


    private void checkX(int x) {
        if (!(checkRange(x, 0, getWidth()))) {
            throw new ArrayIndexOutOfBoundsException("X out of bounds!");
        }
    }

    private int resolveIndex(int y, int x) {
        return y * this.getWidth() + x;
    }

    public void set(final IntPair coordinate, int value) {
        set(coordinate.first, coordinate.second, value);
    }

    public void set(int y, int x, int value) {
        checkY(y);
        checkX(x);

        this.buffer[resolveIndex(y, x)] = value;
    }

    public void setRow(int y, int... row) {
        checkY(y);
        if (row.length != this.getWidth()) {
            throw new ArrayIndexOutOfBoundsException("Row has wrong size");
        }

        for (int i = 0; i < this.getWidth(); ++i) {
            this.buffer[resolveIndex(y, i )] = row[i];
        }
    }

    public Iterator<Pair<IntPair, Integer>> horizontalCoordIterator() {
        return new Iterator<>() {
            private int index = 0;
            private final int maxIndex = getHeight() * getWidth();

            @Override
            public boolean hasNext() {
                return index < maxIndex;
            }

            @Override
            public Pair<IntPair, Integer> next() {
                int y = index / getWidth();
                int x = index % getWidth();
                var coordinate = new IntPair(y, x);
                ++index;
                return new Pair<>(coordinate, get(y, x));
            }
        };
    }


}
