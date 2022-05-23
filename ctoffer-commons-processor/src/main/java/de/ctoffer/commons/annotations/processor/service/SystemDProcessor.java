package de.ctoffer.commons.annotations.processor.service;

import com.google.auto.service.AutoService;
import de.ctoffer.commons.annotations.compile.SystemDService;
import de.ctoffer.commons.annotations.processor.base.AnnotationHandler;
import de.ctoffer.commons.annotations.processor.base.ProcessingContext;
import de.ctoffer.commons.annotations.processor.base.SimpleAnnotationProcessor;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.StandardLocation;
import java.nio.file.Path;
import java.util.Set;

@SupportedAnnotationTypes("de.ctoffer.commons.annotations.compile.SystemDService")
@SupportedSourceVersion(SourceVersion.RELEASE_12)
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
    public void process(Element annotationElement, ProcessingContext context) {
        var serviceData = annotationElement.getAnnotation(SystemDService.class);

        try {
            var projectRoot = Path.of(
                    context.getFiler().getResource(StandardLocation.CLASS_OUTPUT,
                            "",
                            "ignore.that"
                    ).toUri()
            ).getParent().getParent().getParent();

            System.out.println("ProjectRoot: " + projectRoot);
        } catch (Exception e) {
            context.getMessager().error(
                    annotationElement,
                    "Error during accessing resource."
            );
        }
    }
}
