package de.ctoffer.commons.storage;

public interface StorageLoad<I, T> {
    T load(I id);
}
