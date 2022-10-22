package de.ctoffer.commons.storage;

public interface StorageConcept<I, T extends Identifiable<I>> extends StorageSave<T>, StorageLoad<I, T>, StorageDelete<T> {

}
