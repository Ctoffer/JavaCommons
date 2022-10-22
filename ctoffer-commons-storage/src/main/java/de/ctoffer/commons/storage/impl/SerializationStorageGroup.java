package de.ctoffer.commons.storage.impl;

import de.ctoffer.commons.storage.api.Identifiable;
import de.ctoffer.commons.storage.base.StorageGroup;
import de.ctoffer.commons.storage.exception.StorageException;
import de.ctoffer.commons.storage.util.Serial;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.Optional;

public class SerializationStorageGroup <I, T extends Identifiable<I> & Serializable> extends StorageGroup<T> {
    public SerializationStorageGroup(
            final Class<T> cls
    ) {
        super(cls, cls.getSimpleName().toLowerCase());
    }

    @Override
    public String createFileNameFor(
            final T obj
    ) {
        return obj.id().toString() + ".ser";
    }

    @Override
    protected void saveToStorage(
            final Path path,
            final T obj
    ) {
        Serial.write(path,  obj);
    }

    @Override
    protected Optional<T> loadFromStorage(
            final Path dir,
            final String objName
    ) {
        try {
            return Optional.of(Serial.read(dir.resolve(objName)));
        } catch (final StorageException e) {
            return Optional.empty();
        }
    }
}
