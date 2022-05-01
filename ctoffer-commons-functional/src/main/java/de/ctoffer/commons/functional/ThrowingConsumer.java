package de.ctoffer.commons.functional;

@FunctionalInterface
public interface ThrowingConsumer <T, E extends Exception> {
    void accept(T obj) throws E;
}
