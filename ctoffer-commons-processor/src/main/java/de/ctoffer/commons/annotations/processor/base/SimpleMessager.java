package de.ctoffer.commons.annotations.processor.base;

import lombok.RequiredArgsConstructor;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

@RequiredArgsConstructor
public class SimpleMessager {

    private final Messager messager;

    public void warn(final Element element, final String message) {
        this.messager.printMessage(Diagnostic.Kind.WARNING, message, element);
    }

    public void error(final Element element, final String message) {
        this.messager.printMessage(Diagnostic.Kind.ERROR, message, element);
    }
}
