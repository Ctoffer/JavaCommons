package de.ctoffer.commons.algorithms.backtracking;

import java.util.List;

public interface InplaceMutator<T,N>{
    List<N> allMutationsOf(T object);
}
