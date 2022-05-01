package de.ctoffer.commons.test.util;

public interface EntryComparison <X> {
    boolean hasNext();
    void withNextObject(X object);
}
