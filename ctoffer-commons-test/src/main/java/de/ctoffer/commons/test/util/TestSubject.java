package de.ctoffer.commons.test.util;

public class TestSubject <T> {
    private T subject;

    public void setSubject(final T subject) {
        this.subject = subject;
    }

    public T getSubject() {
        return subject;
    }
}
