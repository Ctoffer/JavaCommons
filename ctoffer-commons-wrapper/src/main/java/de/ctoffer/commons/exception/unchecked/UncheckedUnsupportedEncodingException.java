package de.ctoffer.commons.exception.unchecked;

import java.io.UnsupportedEncodingException;

public class UncheckedUnsupportedEncodingException extends UncheckedException {
    public UncheckedUnsupportedEncodingException(final UnsupportedEncodingException exception) {
        super(exception);
    }
}
