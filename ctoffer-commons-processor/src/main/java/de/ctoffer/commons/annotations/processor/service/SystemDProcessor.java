package de.ctoffer.commons.annotations.processor.service;

import com.google.auto.service.AutoService;
import de.ctoffer.commons.annotations.compile.SystemDService;
import de.ctoffer.commons.annotations.processor.base.AnnotationHandler;
import de.ctoffer.commons.annotations.processor.base.ProcessingContext;
import de.ctoffer.commons.annotations.processor.base.SimpleAnnotationProcessor;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static de.ctoffer.commons.annotations.processor.util.Util.getProjectStructure;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;


@SupportedAnnotationTypes("de.ctoffer.commons.annotations.compile.SystemDService")
@SupportedSourceVersion(SourceVersion.RELEASE_16)
@AutoService(Processor.class)
public class SystemDProcessor extends SimpleAnnotationProcessor<SystemDService> {

    public SystemDProcessor() {
        super(new SystemDAnnotationHandler());
    }

    @Override
    protected Class<SystemDService> getAnnotationClass() {
        return SystemDService.class;
    }

    @Override
    protected Set<ElementKind> getSupportedElementKinds() {
        return Set.of(ElementKind.CLASS);
    }
}

class SystemDAnnotationHandler implements AnnotationHandler<SystemDService> {

    @Override
    public void process(
            final Element annotationElement,
            final ProcessingContext processingContext
    ) {
        var serviceData = annotationElement.getAnnotation(SystemDService.class);

        try {
            final var mavenProjectStructure = getProjectStructure(processingContext);
            final var reader = new MavenXpp3Reader();

            final var model = reader.read(new FileReader(mavenProjectStructure.pom().toFile()));

            final var generatedResources = mavenProjectStructure.generatedResources();
            Files.createDirectories(generatedResources);

            var serviceName = model.getArtifactId();
            var jarName = model.getArtifactId() + "-" + model.getVersion() + ".jar";

            writeServiceFile(serviceData, generatedResources, serviceName);
            writeStartScript(
                    generatedResources,
                    serviceName,
                    jarName,
                    serviceData.jvmArgs(),
                    serviceData.systemProperties(),
                    serviceData.programArgs()
            );
            writeStopScript(generatedResources, jarName);
        } catch (Exception e) {
            processingContext.getMessager().error(
                    annotationElement,
                    "Error during accessing resource."
            );
        }
    }

    private static void writeServiceFile(
            final SystemDService serviceData,
            final Path generatedResources,
            final String serviceName
    ) throws IOException {
        var serviceFile = generatedResources.resolve(serviceName);
        Files.write(serviceFile, prepareInitDFile(serviceData, serviceName));
    }

    private static List<String> prepareInitDFile(
            final SystemDService serviceData,
            final String serviceName
    ) {
        return Arrays.asList(
                "#!/bin/bash",
                "# /etc/init.d/" + serviceName,
                "",
                "### BEGIN INIT INFO",
                "# Provides:          " + serviceData.serviceName(),
                "# Required-Start:",
                "# Required-Stop:",
                "# Default-Start:     " + join(serviceData.defaultStart()),
                "# Default-Stop:" + join(serviceData.defaultStop()),
                "# Short-Description: " + serviceData.shortDescription(),
                "# Description:       " + serviceData.description(),
                "### END INIT INFO",
                "",
                "case $1 in",
                "    start)",
                "        /bin/bash /home/pi/Software/" + serviceName + "/bin/start.sh &",
                "    ;;",
                "    stop)",
                "        /bin/bash /home/pi/Software/" + serviceName + "/bin/stop.sh",
                "    ;;",
                "    restart)",
                "        /bin/bash /home/pi/Software/" + serviceName + "/bin/stop.sh",
                "        /bin/bash /home/pi/Software/" + serviceName + "/bin/start.sh &",
                "    ;;",
                "esac",
                "exit 0"
        );
    }

    private static String join(int[] array) {
        return stream(array).mapToObj(Integer::toString).collect(joining(" "));
    }

    private static void writeStartScript(
            final Path generatedResources,
            final String serviceName,
            final String jarName,
            final String[] jvmArgs,
            final String[] systemProperties,
            final String[] programArgs
    ) throws IOException {
        var startScript = generatedResources.resolve("start.sh");
        Files.write(startScript, Arrays.asList(
                "#!/bin/bash",
                "",
                "cd /home/pi/Software/" + serviceName,
                String.join(
                        " ",
                        "java",
                        String.join(" ", jvmArgs),
                        stream(systemProperties).map(s -> "-D" + s).collect(joining(" ")),
                        "-jar",
                        jarName,
                        String.join(" ", programArgs)
                )
        ));
    }

    private static void writeStopScript(
            final Path generatedResources,
            final String jarName
    ) throws IOException {
        var startScript = generatedResources.resolve("stop.sh");
        Files.write(startScript, Arrays.asList(
                "#!/bin/bash",
                "",
                "pid=$( pgrep -f " + jarName + " )",
                "",
                "if [[ ! -z \"${pid}\" ]]; then",
                "  for cid in $(pgrep -P ${pid}); do sudo kill -9 $cid; done",
                "  sudo kill -9 $pid",
                "fi"
        ));
    }
}
