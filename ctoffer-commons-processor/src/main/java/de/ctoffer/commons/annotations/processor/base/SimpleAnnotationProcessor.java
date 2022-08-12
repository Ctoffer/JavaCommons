package de.ctoffer.commons.annotations.processor.base;

import lombok.RequiredArgsConstructor;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public abstract class SimpleAnnotationProcessor<T extends Annotation> extends AbstractProcessor {

    private final AnnotationHandler<T> handler;

    private SimpleMessager messager;
    private Types typeUtilities;
    private Filer filer;
    private Elements elementUtils;

    protected abstract Class<T> getAnnotationClass();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = new SimpleMessager(processingEnv.getMessager());
        this.typeUtilities = processingEnv.getTypeUtils();
        this.filer = processingEnv.getFiler();
        this.elementUtils = processingEnv.getElementUtils();
        processingEnv.getOptions().forEach((k, v) -> System.out.println(k + ": " + v));
    }

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
        final ProcessingContext context = ProcessingContext.builder()
                .messager(messager)
                .typeUtilities(typeUtilities)
                .filer(filer)
                .elementUtils(elementUtils)
                .roundEnvironment(roundEnv)
                .build();

        for (final var annotationElement : roundEnv.getElementsAnnotatedWith(getAnnotationClass())) {
            if (getSupportedElementKinds().contains(annotationElement.getKind())) {
                handler.process(annotationElement, context);
            } else {
                messager.error(
                        annotationElement,
                        "Annotation " + getAnnotationClass().getCanonicalName() + "can only be used on class."
                );
            }
        }

        return true;
    }

    protected abstract Set<ElementKind> getSupportedElementKinds();

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return startStream(new Class[]{getAnnotationClass()}).map(Class::getCanonicalName).collect(Collectors.toSet());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_16;
    }

    private static <T> Stream<T> startStream(T... args) {
        return Arrays.stream(args);
    }
}
