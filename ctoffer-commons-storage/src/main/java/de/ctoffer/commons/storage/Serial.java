package de.ctoffer.commons.storage;

import de.ctoffer.commons.exception.unchecked.UncheckedExceptions;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class Serial {

    public static void write(
            final Path destination,
            final Serializable object) {
        requireNonNull(destination);
        requireNonNull(object);

        Optional.of(requireNonNull(destination, "Destination must be non-null"))
                .map(Path::getParent)
                .ifPresent(UncheckedExceptions.wrapIOException(Files::createDirectories));

        try (final ObjectOutputStream oos = createOutputStream(destination)) {
            oos.writeObject(requireNonNull(object, "Serializable object must be non-null"));
        } catch (final IOException ioe) {
            throw new StorageException(ioe);
        }
    }

    private static ObjectOutputStream createOutputStream(
            final Path destination
    ) throws IOException {
        return new ObjectOutputStream(Files.newOutputStream(destination));
    }

    @SuppressWarnings("unchecked")
    public static <T> T read(
            final Path source
    ) {
        try (final ObjectInputStream ois = createInputStream(source)) {
            return (T) ois.readObject();
        } catch (final IOException | ClassNotFoundException e) {
            throw new StorageException(e);
        }
    }

    private static ObjectInputStream createInputStream(
            final Path source
    ) throws IOException {
        return new ObjectInputStream(Files.newInputStream(source));
    }
}
