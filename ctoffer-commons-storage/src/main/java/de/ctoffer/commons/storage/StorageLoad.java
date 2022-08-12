package de.ctoffer.commons.storage;

public interface StorageLoad<T> {
    T load(int id);
}
