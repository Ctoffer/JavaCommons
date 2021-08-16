package de.ctoffer.algorithms;

public interface InplaceMutation<T> {
    void activate(T object);
    void deactivate(T object);
}
