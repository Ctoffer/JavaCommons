package de.ctoffer.commons.exception.unchecked;


public class UncheckedInterruptedException extends UncheckedException {

    public UncheckedInterruptedException(final InterruptedException interruptedException) {
        super(interruptedException);
    }
}
