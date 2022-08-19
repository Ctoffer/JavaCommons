package de.ctoffer.commons.exception.unchecked;

import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public class UncheckedUnsupportedEncodingException extends RuntimeException {
    public UncheckedUnsupportedEncodingException(final UnsupportedEncodingException exception) {
        super(exception);
    }
}
