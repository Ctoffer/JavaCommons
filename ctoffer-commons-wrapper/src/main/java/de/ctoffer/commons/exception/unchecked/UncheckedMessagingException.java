package de.ctoffer.commons.exception.unchecked;

import jakarta.mail.MessagingException;

public class UncheckedMessagingException extends UncheckedException {
    public UncheckedMessagingException(final MessagingException e) {
        super(e);
    }
}
