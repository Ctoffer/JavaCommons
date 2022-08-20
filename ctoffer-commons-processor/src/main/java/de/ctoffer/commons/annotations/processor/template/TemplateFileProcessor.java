package de.ctoffer.commons.annotations.processor.template;

import com.google.auto.service.AutoService;
import de.ctoffer.commons.annotations.compile.TemplateFile;
import de.ctoffer.commons.annotations.processor.base.AnnotationHandler;
import de.ctoffer.commons.annotations.processor.base.ProcessingContext;
import de.ctoffer.commons.annotations.processor.base.SimpleAnnotationProcessor;
import de.ctoffer.commons.container.Pair;
import de.ctoffer.commons.maven.EnvironmentLookup;
import de.ctoffer.commons.maven.MavenProjectStructure;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static de.ctoffer.commons.annotations.processor.util.Util.getProjectStructure;
import static java.util.Optional.ofNullable;

@SupportedAnnotationTypes("de.ctoffer.commons.annotations.compile.Templatefile")
@SupportedSourceVersion(SourceVersion.RELEASE_16)
@AutoService(Processor.class)
public class TemplateFileProcessor extends SimpleAnnotationProcessor<TemplateFile> {

    public TemplateFileProcessor() {
        super(new TemplateFileHandler());
    }

    @Override
    protected Class<TemplateFile> getAnnotationClass() {
        return TemplateFile.class;
    }

    @Override
    protected Set<ElementKind> getSupportedElementKinds() {
        return Set.of(ElementKind.CLASS, ElementKind.RECORD);
    }
}

class TemplateFileHandler implements AnnotationHandler<TemplateFile> {

    @Override
    public void process(Element annotationElement, ProcessingContext processingContext) {
        final TemplateFile annotation = annotationElement.getAnnotation(TemplateFile.class);
        final String requestedResource = annotation.value();
        final TemplateFile.TargetLocation directory = annotation.targetDirectory();

        try {
            final EnvironmentLookup lookup = new EnvironmentLookup();
            final MavenProjectStructure mavenProjectStructure = getProjectStructure(processingContext);

            final Path templatePath = mavenProjectStructure.mainResources().resolve(requestedResource + ".template");
            final Path concretePath = mapPath(mavenProjectStructure, directory).resolve(requestedResource);

            updateMavenEnvironment(lookup, mavenProjectStructure);

            Files.createDirectories(concretePath.getParent());
            Files.write(
                    concretePath,
                    Files.readAllLines(templatePath)
                            .stream()
                            .map(line -> formatLine(line, lookup))
                            .collect(Collectors.toList())
            );
        } catch (final Exception e) {
            processingContext.getMessager().error(
                    annotationElement,
                    "Error " + e + " during accessing resource: " + requestedResource
            );
        }
    }

    private static Path mapPath(final MavenProjectStructure structure, final TemplateFile.TargetLocation targetLocation) {
        return switch (targetLocation) {
            case MAIN_RESOURCES -> structure.mainResources();
            case TEST_RESOURCES -> structure.testResources();
            case GENERATED_RESOURCES -> structure.generatedResources();
            case COMPILED_CLASSES -> structure.classes();
        };
    }

    private void updateMavenEnvironment(final EnvironmentLookup lookup, final MavenProjectStructure structure) throws IOException, XmlPullParserException {
        final var reader = new MavenXpp3Reader();

        final var model = reader.read(new FileReader(structure.pom().toFile()));
        Model parent = null;
        if (Objects.nonNull(model.getParent())) {
            var parentPomPath = structure.projectRoot().getParent().resolve("pom.xml").toFile();

            if (parentPomPath.exists()) {
                // Only works if in same directory tree
                parent = reader.read(new FileReader(parentPomPath));
            }
        }

        lookup.simpleNamespace("mvn", "maven")
                .insert("project.groupId", ofNullable(model.getGroupId()).orElseGet(() -> model.getParent().getGroupId()))
                .insert("project.artifactId", ofNullable(model.getArtifactId()).orElseGet(() -> model.getParent().getArtifactId()))
                .insert("project.version", ofNullable(model.getVersion()).orElseGet(() -> model.getParent().getVersion()))
                .insert(extractPropertiesOf(parent))
                .insert(extractPropertiesOf(model));
    }

    private Map<String, String> extractPropertiesOf(final Model model) {
        if (Objects.isNull(model)) {
            return new HashMap<>();
        } else {
            return model.getProperties()
                    .entrySet()
                    .stream()
                    .map(Pair::of)
                    .map(Pair.mapper(Object::toString, Object::toString))
                    .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
        }
    }

    private String formatLine(String line, final Map<String, String> lookup) {
        for (final Map.Entry<String, String> entry : lookup.entrySet()) {
            line = line.replace("{" + entry.getKey() + "}", entry.getValue());
        }

        return line;
    }
}
