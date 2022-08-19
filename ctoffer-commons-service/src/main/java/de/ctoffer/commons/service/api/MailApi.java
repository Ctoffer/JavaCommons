package de.ctoffer.commons.service.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.ctoffer.commons.dto.service.MailAttachment;
import de.ctoffer.commons.exception.unchecked.UncheckedInterruptedException;
import lombok.Builder;
import lombok.Getter;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Builder
@Getter
public class MailApi {

    public class PrepareSendRequest {
        private String subject;
        private String body;
        private final List<MailAttachment> attachments = new ArrayList<>();

        public PrepareSendRequest subject(final String subject) {
            this.subject = Objects.requireNonNull(subject);
            return this;
        }

        public PrepareSendRequest body(final String body) {
            this.body = Objects.requireNonNull(body);
            return this;
        }

        public PrepareSendRequest addAttachment(final Path path) {
            this.attachments.add(MailAttachment.of(path));
            return this;
        }

        public HttpResponse<String> send() {
            this.subject = Objects.requireNonNullElse(subject, "");
            this.body = Objects.requireNonNullElse(body, "");

            try {
                var values = new HashMap<String, Object>() {{
                    put("domain", domain);
                    put("subject", subject);
                    put("body", body);
                    put("attachments", attachments);
                }};

                String requestBody = objectMapper.writeValueAsString(values);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://" + host + ":" + port + "/system/send"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();

                return client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (final IOException ioe) {
                throw new UncheckedIOException(ioe);
            } catch (final InterruptedException ie) {
                throw new UncheckedInterruptedException(ie);
            }
        }
    }

    @Builder.Default
    private final String host = "localhost";

    @Builder.Default
    private final int port = 8273;

    private final String domain;

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PrepareSendRequest prepareMailRequest() {
        return new PrepareSendRequest();
    }
}
