package de.ctoffer.commons.exception.unchecked;

import jakarta.mail.MessagingException;

public class UncheckedMessagingException extends RuntimeException {
    public UncheckedMessagingException(final MessagingException e) {
        super(e);
    }
}
