package de.ctoffer.commons.storage;

import de.ctoffer.commons.exception.unchecked.UncheckedExceptions;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class StorageGroup<T> implements Nameable {

    private final Class<T> cls;
    private final String name;

    @Override
    public String name() {
        return this.name;
    }

    public Class<T> classOfStoredObjects() {
        return cls;
    }

    public String saveIn(
            final Path home,
            final T o
    ) {
        final Path directory = getDirectory(home);
        String fname = createFileNameFor(o);
        final Path filePath = directory.resolve(fname);
        Optional.of(filePath)
                .map(Path::getParent)
                .ifPresent(UncheckedExceptions.wrapIOException(Files::createDirectories));
        saveToStorage(filePath, o);
        return fname;
    }

    public Path getDirectory(final Path home) {
        return home.resolve(name);
    }

    public abstract String createFileNameFor(T obj);

    protected abstract void saveToStorage(Path directory, T obj);

    public Optional<T> loadFrom(
            final Path home,
            final String objName
    ) {
        var directory = getDirectory(home);
        return loadFromStorage(directory, objName);
    }

    protected abstract Optional<T> loadFromStorage(Path dir, String objName);
}
