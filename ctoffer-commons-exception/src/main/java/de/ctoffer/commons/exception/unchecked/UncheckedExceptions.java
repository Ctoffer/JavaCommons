package de.ctoffer.commons.exception.unchecked;

import de.ctoffer.commons.functional.ThrowingConsumer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Consumer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UncheckedExceptions {

    public static <T> Consumer<T> wrapIOException(
            final ThrowingConsumer<T, IOException> throwingAction
    ) {
        return obj -> {
            try {
                throwingAction.accept(obj);
            } catch (final IOException ioe) {
                throw new UncheckedIOException(ioe);
            }
        };
    }
}
