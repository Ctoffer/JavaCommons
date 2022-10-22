package de.ctoffer.commons.storage.api;

public interface StorageLoad<I, T> {
    T load(I id);
}
