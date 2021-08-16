package de.ctoffer.algorithms;

import java.util.List;

public interface InplaceMutator<T,N>{
    List<N> allMutationsOf(T object);
}
