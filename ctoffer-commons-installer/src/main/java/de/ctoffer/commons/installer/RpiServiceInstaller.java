package de.ctoffer.commons.installer;

import de.ctoffer.commons.io.StdIo;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class RpiServiceInstaller {

    @Accessors(fluent = true)
    @Data
    @Builder
    @EqualsAndHashCode
    private static class Input {
        private final String artifactId;
        private final String version;
        private final Path buildDirectory;
    }

    @Accessors(fluent = true)
    @Data
    @EqualsAndHashCode
    private static class DirectoryStruct {

        private final Path buildDirectory;
        private final Path generatedResourcesDirectory;
        private final Path artifactDirectory;
        private final Path artifactPath;
        private final Path runnerScriptPath;
        private final Path startScriptPath;
        private final Path stopScriptPath;

        public DirectoryStruct(final Input input) {
            this.buildDirectory = input.buildDirectory();
            this.generatedResourcesDirectory = buildDirectory.resolve("generated-resources");
            this.artifactDirectory = buildDirectory.resolve("artifact");
            this.artifactPath = artifactDirectory.resolve(input.artifactId() + "-" + input.version() + ".jar");
            this.runnerScriptPath = generatedResourcesDirectory.resolve(input.artifactId());
            this.startScriptPath = generatedResourcesDirectory.resolve("start.sh");
            this.stopScriptPath = generatedResourcesDirectory.resolve("stop.sh");
        }
    }

    @Accessors(fluent = true)
    @Data
    @Builder
    @EqualsAndHashCode
    private static class RpiInstallerData {
        private final String artifactId;
        private final String version;
        private final String jarData;
        private final String initData;
        private final String startData;
        private final String stopData;
    }

    public static void main(String[] args) {
        StdIo.print(
                System.getProperty("artifactId"),
                System.getProperty("version"),
                System.getProperty("buildDirectory")
        );

        var input = Input.builder()
                .artifactId(System.getProperty("artifactId"))
                .version(System.getProperty("version"))
                .buildDirectory(Paths.get(System.getProperty("buildDirectory")))
                .build();

        var installerData = createInstallerData(input);
        var lines = String.join("\n", createInstallerScript(installerData));

        try {
            Files.write(
                    input.buildDirectory().resolve(input.artifactId() + "-" + input.version + ".sh"),
                    lines.getBytes(StandardCharsets.UTF_8)
            );
        } catch (final IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    private static RpiInstallerData createInstallerData(final Input input) {
        var directoryStruct = new DirectoryStruct(input);
        final var jarData = base64(directoryStruct.artifactPath());
        final var initData = base64(directoryStruct.runnerScriptPath());
        final var startData = base64(directoryStruct.startScriptPath());
        final var stopData = base64(directoryStruct.stopScriptPath());

        return RpiInstallerData.builder()
                .artifactId(input.artifactId())
                .version(input.version())
                .jarData(jarData)
                .initData(initData)
                .startData(startData)
                .stopData(stopData)
                .build();
    }


    static String base64(final Path file) {
        try {
            return base64(Files.readAllBytes(file));
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    static String base64(final byte[] zippedData) {
        return Base64.getMimeEncoder(76, new byte[]{'\n'}).encodeToString(zippedData);
    }


    private static List<String> createInstallerScript(final RpiInstallerData data) {
        return Arrays.asList(
                "artifact_id=" + data.artifactId(),
                "version=" + data.version(),
                "",
                "",
                "home=\"/home/pi/Software/${artifact_id}\"",
                "jar_name=\"${artifact_id}-${version}.jar\"",
                "",
                "",
                "jar_data=$(cat << EOF",
                data.jarData(),
                "EOF",
                ")",
                "init_data=$(cat << EOF",
                data.initData(),
                "EOF",
                ")",
                "start_data=$(cat << EOF",
                data.startData(),
                "EOF",
                ")",
                "stop_data=$(cat << EOF",
                data.stopData(),
                "EOF",
                ")",
                "",
                "",
                "echo -n \"Setup target directory ${artifact_id} ... \"",
                "mkdir -p \"${home}/bin\"",
                "mkdir -p \"${home}/data\"",
                "echo \"[OK]\"",
                "",
                "if [[ -f \"${home}/bin/stop.sh\" ]]; then",
                "  echo -n \"Shutting down ${artifact_id} ... \"",
                "  bash \"${home}/bin/stop.sh\"",
                "  echo \"[OK]\"",
                "fi",
                "",
                "echo -n \"Copying ${artifact_id} ... \"",
                "echo -n \"${jar_data}\" | base64 -d > \"${home}/${jar_name}\"",
                "sudo chmod 777 \"${home}/${jar_name}\"",
                "echo \"[OK]\"",
                "",
                "echo -n \"Copying start script for ${artifact_id} ... \"",
                "echo -n \"${start_data}\" | base64 -d | gzip -df  > \"${home}/bin/start.sh\"",
                "dos2unix \"${home}/bin/start.sh\" &> /dev/null",
                "echo \"[OK]\"",
                "",
                "echo -n \"Copying stop script for ${artifact_id} ... \"",
                "echo -n \"${stop_data}\" | base64 -d | gzip -df  > \"${home}/bin/stop.sh\"",
                "dos2unix \"${home}/bin/stop.sh\" &> /dev/null",
                "echo \"[OK]\"",
                "",
                "echo -n \"Updating init.d entry ${artifact_id} ... \"",
                "echo -n \"${init_data}\" | base64 -d | gzip -df > \"/etc/init.d/${artifact_id}\"",
                "sudo dos2unix \"/etc/init.d/${artifact_id}\" &> /dev/null",
                "sudo update-rc.d $artifact_id defaults",
                "echo \"[OK]\"",
                "",
                "echo -n \"Start service ${artifact_id}.\"",
                "bash \"${home}/bin/start.sh\" &",
                "echo \"[OK]\""
        );
    }
}
