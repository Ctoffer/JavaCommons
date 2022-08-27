package de.ctoffer.commons.transfer;

import com.jcraft.jsch.*;
import de.ctoffer.commons.io.StdIo;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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

    private static final int SESSION_TIMEOUT = 10000;
    private static final int CHANNEL_TIMEOUT = 1000;


    public static void main(String[] args) {
        var input = Input.builder()
                .artifactId(System.getProperty("artifactId"))
                .version(System.getProperty("version"))
                .buildDirectory(Paths.get(System.getProperty("buildDirectory")))
                .sftpHost(System.getProperty("sftpHost"))
                .sftpPort(Integer.parseInt(System.getProperty("sftpPort")))
                .sftpUser(System.getProperty("sftpUser"))
                .sftpPassword(System.getProperty("sftpPassword"))
                .build();
        var installerName = input.artifactId() + "-" + input.version() + ".sh";
        var localInstaller = input.buildDirectory().resolve(installerName);

        StdIo.print(
                input.artifactId,
                input.version,
                input.buildDirectory
        );

        Session jschSession = null;

        try {

            JSch jsch = new JSch();
            jsch.setKnownHosts(Path.of(System.getProperty("user.home"),".ssh", "known_hosts").toString());
            jschSession = jsch.getSession(input.sftpUser(), input.sftpHost(), input.sftpPort());
            jschSession.setPassword(input.sftpPassword());
            jschSession.connect(SESSION_TIMEOUT);

            Channel sftp = jschSession.openChannel("sftp");
            sftp.connect(CHANNEL_TIMEOUT);

            ChannelSftp channelSftp = (ChannelSftp) sftp;
            channelSftp.put(localInstaller.toString(), "./");


            channelSftp.exit();

        } catch (JSchException | SftpException e) {

            e.printStackTrace();

        } finally {
            if (jschSession != null) {
                jschSession.disconnect();
            }
        }
    }
}
