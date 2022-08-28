package de.ctoffer.commons.transfer;

import com.jcraft.jsch.*;
import de.ctoffer.commons.functional.ThrowingConsumer;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RpiArtifactTransfer {

    @Accessors(fluent = true)
    @Data
    @Builder
    @EqualsAndHashCode
    private static class Input {
        private final String artifactId;
        private final String version;
        private final Path buildDirectory;
        private final String sftpHost;
        private final int sftpPort;
        private final String sftpUser;
        private final String sftpPassword;
    }

    @RequiredArgsConstructor
    public static class AutoCloseableWrapper<T, E extends Exception> implements AutoCloseable {

        @Accessors(fluent = true)
        @Getter
        private final T object;
        private final ThrowingConsumer<T, E> close;

        public <F extends Exception> AutoCloseableWrapper(
                final T object,
                final ThrowingConsumer<T, F> postInit,
                final ThrowingConsumer<T, E> close
        ) throws F {
            this(object, close);

            postInit.accept(object);
        }

        @Override
        public void close() throws E {
            this.close.accept(object);
        }
    }

    private static final int SESSION_TIMEOUT = 10000;
    private static final int CHANNEL_TIMEOUT = 1000;

    public static void main(String[] args) {
        var input = readInputStateFromSystemProperties();

        var installerName = input.artifactId() + "-" + input.version() + ".sh";
        var localInstaller = input.buildDirectory().resolve(installerName);

        try {

            JSch jsch = new JSch();
            jsch.setKnownHosts(Path.of(System.getProperty("user.home"), ".ssh", "known_hosts").toString());

            try (final var session = createSessionWrapper(input, jsch);
                 final var channel = createChannelWrapper(session);
            ) {
                final var sftp = channel.object();

                System.out.println("Transfer " + localInstaller + " to remote " + input.sftpUser + "@" + input.sftpHost + ":" + input.sftpPort);
                sftp.put(localInstaller.toString(), "./");
                System.out.println("Transfer cnf file");
                sftp.put(toInputStream(new StringReader("")), "./cnf/" + installerName + ".cnf");
            }
        } catch (final JSchException | SftpException | IOException e) {
            e.printStackTrace();
        }
    }

    private static Input readInputStateFromSystemProperties() {
        return Input.builder()
                .artifactId(System.getProperty("artifactId"))
                .version(System.getProperty("version"))
                .buildDirectory(Paths.get(System.getProperty("buildDirectory")))
                .sftpHost(System.getProperty("sftpHost"))
                .sftpPort(Integer.parseInt(System.getProperty("sftpPort")))
                .sftpUser(System.getProperty("sftpUser"))
                .sftpPassword(System.getProperty("sftpPassword"))
                .build();
    }

    private static AutoCloseableWrapper<Session, RuntimeException> createSessionWrapper(
            final Input input,
            final JSch jSch
    ) throws JSchException {
        return new AutoCloseableWrapper<>(
                jSch.getSession(input.sftpUser(), input.sftpHost(), input.sftpPort()),
                session -> {
                    session.setPassword(input.sftpPassword());
                    session.connect(SESSION_TIMEOUT);
                },
                Session::disconnect
        );
    }

    private static AutoCloseableWrapper<ChannelSftp, RuntimeException> createChannelWrapper(
            final AutoCloseableWrapper<Session, RuntimeException> sessionWrapper
    ) throws JSchException {
        return new AutoCloseableWrapper<>(
                (ChannelSftp) sessionWrapper.object().openChannel("sftp"),
                sftp -> sftp.connect(CHANNEL_TIMEOUT),
                ChannelSftp::exit
        );
    }

    private static InputStream toInputStream(final Reader reader) throws IOException {
        char[] charBuffer = new char[8 * 1024];

        StringBuilder builder = new StringBuilder();
        int numCharsRead;
        while ((numCharsRead = reader.read(charBuffer, 0, charBuffer.length)) != -1) {
            builder.append(charBuffer, 0, numCharsRead);
        }
        reader.close();

        return new ByteArrayInputStream(builder.toString().getBytes(StandardCharsets.UTF_8));
    }
}
