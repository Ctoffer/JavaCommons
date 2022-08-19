package de.ctoffer.commons.wrapper.mail;

import de.ctoffer.commons.exception.unchecked.UncheckedMessagingException;
import de.ctoffer.commons.exception.unchecked.UncheckedUnsupportedEncodingException;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

public class MailBuilder {

    public static MailBuilder builder() {
        return new MailBuilder();
    }

    private InternetAddress from;
    private final Map<Message.RecipientType, InternetAddress[]> recipients;
    private String subject;
    private final Multipart multipart;

    private MailBuilder() {
        this.recipients = new HashMap<>();
        this.multipart = new MimeMultipart();
    }

    public MailBuilder from(final String address, final String personal) {
        try {
            from = new InternetAddress(address, personal);
        } catch (final UnsupportedEncodingException unsupportedEncodingException) {
            throw new UncheckedUnsupportedEncodingException(unsupportedEncodingException);
        }
        return this;
    }

    private InternetAddress[] parseInternetAddress(final String[] addresses) {
        return parseInternetAddress(String.join(" ", addresses));
    }

    private InternetAddress[] parseInternetAddress(final String addresses) {
        try {
            return InternetAddress.parse(addresses);
        } catch (final MessagingException e) {
            throw new UncheckedMessagingException(e);
        }
    }

    public MailBuilder to(final String... addresses) {
        setRecipients(
                Message.RecipientType.TO,
                parseInternetAddress(Objects.requireNonNull(addresses))
        );

        return this;
    }

    private void setRecipients(final Message.RecipientType recipientType, final InternetAddress[] addresses) {
        recipients.put(recipientType, addresses);
    }

    public MailBuilder cc(final String... addresses) {
        return blindCarbonCopy(addresses);
    }

    public MailBuilder carbonCopy(final String... addresses) {
        setRecipients(
                Message.RecipientType.CC,
                parseInternetAddress(Objects.requireNonNull(addresses))
        );
        return this;
    }

    public MailBuilder bcc(final String... addresses) {
        return blindCarbonCopy(addresses);
    }

    public MailBuilder blindCarbonCopy(final String... addresses) {
        setRecipients(
                Message.RecipientType.BCC,
                parseInternetAddress(Objects.requireNonNull(addresses))
        );
        return this;
    }

    public MailBuilder subject(final String subject) {

        this.subject = Objects.requireNonNull(subject);
        return this;
    }

    public MailBuilder text(final String text) {
        final var messageBodyPart = new MimeBodyPart();
        try {
            messageBodyPart.setText(text);
        } catch (final MessagingException exception) {
            throw new UncheckedMessagingException(exception);
        }

        return this;
    }

    public MailBuilder attachment(final Path path) {
        try {
            final var bodyPart = new MimeBodyPart();
            final DataSource source = new FileDataSource(path.toFile());
            bodyPart.setDataHandler(new DataHandler(source));
            bodyPart.setFileName(path.getFileName().toString());
            multipart.addBodyPart(bodyPart);
            return this;
        } catch (final MessagingException e) {
            throw new UncheckedMessagingException(e);
        }
    }

    public void send(final Session session) {
        throwIfTrue(recipients::isEmpty, "No recipient set.");
        throwIfTrue(() -> Objects.isNull(from), "No from set.");
        throwIfTrue(() -> Objects.isNull(subject), "No subject set.");

        try {
            final Message message = new MimeMessage(session);
            message.setFrom(from);
            recipients.entrySet().forEach(e -> setRecipients(message, e));
            message.setSubject(subject);
            message.setContent(multipart);
            Transport.send(message);
        } catch (final MessagingException e) {
            throw new UncheckedMessagingException(e);
        }
    }

    private void throwIfTrue(final BooleanSupplier condition, final String message) {
        if (condition.getAsBoolean()) {
            throw new UncheckedMessagingException(new MessagingException(message));
        }
    }

    private static void setRecipients(
            final Message message,
            final Map.Entry<Message.RecipientType, InternetAddress[]> entry
    ) {
        try {
            message.setRecipients(entry.getKey(), entry.getValue());
        } catch (MessagingException exception) {
            throw new UncheckedMessagingException(exception);
        }
    }
}
