package de.ctoffer.commons.container.primitive;

import java.util.Iterator;

public interface CharIterator {
    boolean hasNext();

    char next();

    Iterator<Character> boxed();
}