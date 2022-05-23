package de.ctoffer.commons.annotations.processor.base;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;

public interface AnnotationHandler<T extends Annotation> {
    void process(Element annotationElement, ProcessingContext context);
}
