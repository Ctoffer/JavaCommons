package de.ctoffer.commons.storage;

public interface StorageConcept<T extends Identifiable> extends StorageSave<T>, StorageLoad<T>, StorageDelete<T>{

}
