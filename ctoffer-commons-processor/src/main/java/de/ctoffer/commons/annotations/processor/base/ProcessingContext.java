package de.ctoffer.commons.annotations.processor.base;

import lombok.Builder;
import lombok.Data;

import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

@Builder
@Data
public class ProcessingContext {

    private final SimpleMessager messager;
    private final Types typeUtilities;
    private final Filer filer;
    private final Elements elementUtils;
    private final RoundEnvironment roundEnvironment;

}
