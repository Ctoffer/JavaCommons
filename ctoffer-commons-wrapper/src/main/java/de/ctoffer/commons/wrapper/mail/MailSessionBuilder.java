package de.ctoffer.commons.wrapper.mail;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;

import java.util.Objects;
import java.util.Properties;
import java.util.function.Supplier;

public class MailSessionBuilder {

    public final static String MAIL_SMTP_HOST = "mail.smtp.host";
    public final static String MAIL_SMTP_PORT = "mail.smtp.port";
    public final static String MAIL_SMTP_AUTH = "mail.smtp.auth";
    public final static String MAIL_SMTP_STARTTLS = "mail.smtp.starttls.enable";
    public final static String MAIL_DEBUG = "mail.debug";

    private final Properties sessionProperties = new Properties();
    private Supplier<PasswordAuthentication> getAuthenticator;

    public static MailSessionBuilder gmail() {
        return new MailSessionBuilder()
                .host("smtp.gmail.com")
                .port(587)
                .auth(true)
                .starttls(true);
    }

    public MailSessionBuilder host(final String host) {
        sessionProperties.put(MAIL_SMTP_HOST, host);
        return this;
    }

    public MailSessionBuilder port(final int port) {
        sessionProperties.put(MAIL_SMTP_PORT, "" +port);
        return this;
    }

    public MailSessionBuilder auth(final boolean auth) {
        sessionProperties.put(MAIL_SMTP_AUTH, "" + auth);
        return this;
    }

    public MailSessionBuilder starttls(final boolean enable) {
        sessionProperties.put(MAIL_SMTP_STARTTLS, "" + enable);
        return this;
    }

    public MailSessionBuilder debug(final boolean enable) {
        sessionProperties.put(MAIL_DEBUG, "" + enable);
        return this;
    }

    public MailSessionBuilder authenticator(final Supplier<PasswordAuthentication> getAuthenticator) {
        this.getAuthenticator = Objects.requireNonNull(getAuthenticator);
        return this;
    }

    public Session build() {
        if (Objects.isNull(getAuthenticator)) {
            return Session.getInstance(sessionProperties);
        } else {
            return Session.getInstance(sessionProperties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return getAuthenticator.get();
                }
            });
        }
    }
}
