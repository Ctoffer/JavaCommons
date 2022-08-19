package de.ctoffer.commons.dto.service;

import de.ctoffer.commons.dto.MimeMediaType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailAttachment {
    private String fileName;
    private String data;
    private String mime;

    public static MailAttachment of(final Path path) {
        final var fileName = path.getFileName().toString();
        final var extension = fileName.substring(fileName.indexOf('.'));

        return MailAttachment.builder()
                .fileName(fileName)
                .data(base64(path))
                .mime(MimeMediaType.fromTypeExtension(extension).mime())
                .build();
    }

    public byte[] getDecodedData() {
        return Base64.getMimeDecoder().decode(data);
    }

    private static String base64(final Path file) {
        try {
            return base64(Files.readAllBytes(file));
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    private static String base64(final byte[] data) {
        return Base64.getMimeEncoder(76, new byte[]{'\n'}).encodeToString(data);
    }

}