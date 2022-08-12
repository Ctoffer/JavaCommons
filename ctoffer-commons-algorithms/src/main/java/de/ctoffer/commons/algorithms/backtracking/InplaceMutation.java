package de.ctoffer.commons.algorithms.backtracking;

public interface InplaceMutation<T> {
    void activate(T object);

    void deactivate(T object);
}
