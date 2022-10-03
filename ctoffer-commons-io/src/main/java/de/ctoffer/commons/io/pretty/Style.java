package de.ctoffer.commons.io.pretty;

import java.util.Objects;

public interface Style {

    char vertical();

    char horizontal();

    char topLeft();

    char topRight();

    char botLeft();

    char botRight();

    char left();

    char right();

    char top();

    char bot();

    char center();

    static Style createStyle(
            final String symbols
    ) {
        if (Objects.isNull(symbols) || symbols.length() != 11) {
            throw new IllegalArgumentException("Style requires exactly 11 symbols");
        }

        char[] symbolLookup = symbols.toCharArray();

        return new Style() {
            @Override
            public char vertical() {
                return symbolLookup[0];
            }

            @Override
            public char horizontal() {
                return symbolLookup[1];
            }

            @Override
            public char topLeft() {
                return symbolLookup[2];
            }

            @Override
            public char topRight() {
                return symbolLookup[3];
            }

            @Override
            public char botLeft() {
                return symbolLookup[4];
            }

            @Override
            public char botRight() {
                return symbolLookup[5];
            }

            @Override
            public char left() {
                return symbolLookup[6];
            }

            @Override
            public char right() {
                return symbolLookup[7];
            }

            @Override
            public char top() {
                return symbolLookup[8];
            }

            @Override
            public char bot() {
                return symbolLookup[9];
            }

            @Override
            public char center() {
                return symbolLookup[10];
            }
        };
    }

    static Style primitiveSingleLineStyle() {
        return Style.createStyle("|-+++++++++");
    }

    static Style primitiveDoubleLineStyle() {
        return Style.createStyle("|=*********");
    }

    static Style singleLineStyle() {
        return Style.createStyle("│─┌┐└┘├┤┬┴┼");
    }

    static Style doubleLineStyle() {
        return Style.createStyle("║═╔╗╚╝╠╣╦╩╬");
    }
}
