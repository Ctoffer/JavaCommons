package de.ctoffer.commons.annotations.processor.template;

import de.ctoffer.commons.annotations.compile.TemplateFile;
import de.ctoffer.commons.annotations.processor.base.AnnotationHandler;
import de.ctoffer.commons.annotations.processor.base.ProcessingContext;
import de.ctoffer.commons.annotations.processor.base.SimpleAnnotationProcessor;
import de.ctoffer.commons.annotations.processor.maven.EnvironmentLookup;
import de.ctoffer.commons.annotations.processor.maven.MavenProjectStructure;
import de.ctoffer.commons.container.Pair;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.tools.StandardLocation;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

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
        return Set.of(ElementKind.CLASS);
    }
}

class TemplateFileHandler implements AnnotationHandler<TemplateFile> {

    @Override
    public void process(Element annotationElement, ProcessingContext processingContext) {
        final String requestedResource = annotationElement.getAnnotation(TemplateFile.class).value();

        try {
            final EnvironmentLookup lookup = new EnvironmentLookup();
            final MavenProjectStructure mavenProjectStructure = getProjectStructure(processingContext);

            final Path templatePath = mavenProjectStructure.mainResources().resolve(requestedResource + ".template");
            final Path concretePath = mavenProjectStructure.generatedResources().resolve(requestedResource);

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

    private MavenProjectStructure getProjectStructure(final ProcessingContext processingContext) throws IOException {
        final Path projectRoot = Paths.get(
                processingContext.getFiler()
                        .getResource(StandardLocation.CLASS_OUTPUT, "", "ignore.that").toUri()
        ).getParent().getParent().getParent();

        return new MavenProjectStructure(projectRoot);
    }

    private void updateMavenEnvironment(final EnvironmentLookup lookup, final MavenProjectStructure structure) throws IOException, XmlPullParserException {
        final var reader = new MavenXpp3Reader();

        final var model = reader.read(new FileReader(structure.pom().toFile()));
        Model parent = null;
        if (Objects.nonNull(model.getParent())) {
            parent = reader.read(new FileReader(structure.projectRoot().getParent().resolve("pom.xml").toFile()));
        }

        lookup.simpleNamespace("mvn", "maven")
                .insert("groupId", ofNullable(model.getGroupId()).orElse(model.getParent().getGroupId()))
                .insert("project.version", ofNullable(model.getVersion()).orElse(model.getParent().getVersion()))
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
