package de.ctoffer.commons.test.util;

import java.util.Objects;

public class Expect {
    private final Class<? extends Exception> exceptionClass;
    private final String exceptionMessage;

    public static Expect Expecting(final Class<? extends Exception> exceptionClass, final String exceptionMessage) {
        return new Expect(exceptionClass, exceptionMessage);
    }

    private Expect(final Class<? extends Exception> exceptionClass, final String exceptionMessage) {
        this.exceptionClass = Objects.requireNonNull(exceptionClass);
        this.exceptionMessage = Objects.requireNonNull(exceptionMessage);
    }

    public Class<? extends Exception> getExceptionClass() {
        return exceptionClass;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }
}
