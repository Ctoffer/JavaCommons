package de.ctoffer.commons.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.SneakyThrows;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

@Builder
@Getter
public class MailApi {

    @Builder.Default
    private final String host = "localhost";

    @Builder.Default
    private final int port = 8273;

    private final String domain;

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public HttpResponse<String> send(final String subject, final String body) {
        var values = new HashMap<String, String>() {{
            put("domain", domain);
            put("subject", subject);
            put("body", body);
        }};

        String requestBody = objectMapper.writeValueAsString(values);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + host + ":" + port + "/system/send"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
