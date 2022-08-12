package de.ctoffer.commons.io;

import lombok.*;
import lombok.experimental.Delegate;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class StdIo {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class PrintConfiguration {
        private final OutputStream file;
        private final String separator;
        private final String end;
        private final boolean flush;
    }

    private static final Supplier<PrintConfiguration.PrintConfigurationBuilder> standardBuilder = () -> new PrintConfiguration.PrintConfigurationBuilder()
            .file(System.out)
            .end("\n")
            .separator(" ")
            .flush(true);
    private static final PrintConfiguration standardConfiguration = standardBuilder.get().build();

    public static void print(final Object... objects) {
        print(standardConfiguration, objects);
    }

    public static void print(final PrintConfiguration configuration, final Object... objects) {
        try {
            final String line = Arrays.stream(objects).map(Objects::toString).collect(Collectors.joining(configuration.separator));
            final byte[] data = (line + configuration.end).getBytes(StandardCharsets.UTF_8);
            configuration.file.write(data);
            if (configuration.flush) {
                configuration.file.flush();
            }
        } catch (final IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    public static String input(final String message, final String defaultValue) {
        if (Objects.nonNull(message) && message.length() > 0) {
            print(standardBuilder.get().end("").build(), message);
        }

        try (final Scanner scanner = new Scanner(new NeverCloseInputStream(System.in))) {
            var result =  scanner.nextLine();
            if (result.isEmpty()) {
                return defaultValue;
            } else {
                return result;
            }
        }
    }

    public static String input(final String message) {
        if (Objects.nonNull(message) && message.length() > 0) {
            print(standardBuilder.get().end("").build(), message);
        }

        try (final Scanner scanner = new Scanner(new NeverCloseInputStream(System.in))) {
            return scanner.nextLine();
        }
    }

    public static String input() {
        return input(null);
    }
}
