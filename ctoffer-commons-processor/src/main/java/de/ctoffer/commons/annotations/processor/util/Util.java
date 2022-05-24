package de.ctoffer.commons.annotations.processor.util;

import de.ctoffer.commons.annotations.processor.base.ProcessingContext;
import de.ctoffer.commons.annotations.processor.maven.MavenProjectStructure;

import javax.tools.StandardLocation;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Util {
    public static MavenProjectStructure getProjectStructure(final ProcessingContext processingContext) throws IOException {
        final Path projectRoot = Paths.get(
                processingContext.getFiler()
                        .getResource(StandardLocation.CLASS_OUTPUT, "", "ignore.that").toUri()
        ).getParent().getParent().getParent();

        return new MavenProjectStructure(projectRoot);
    }
}
