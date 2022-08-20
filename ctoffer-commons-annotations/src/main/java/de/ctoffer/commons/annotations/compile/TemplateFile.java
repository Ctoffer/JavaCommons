package de.ctoffer.commons.annotations.compile;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface TemplateFile {
     enum TargetLocation {
        MAIN_RESOURCES,
        TEST_RESOURCES,
        COMPILED_CLASSES,
        GENERATED_RESOURCES
    }

    String value();
    TargetLocation targetDirectory() default TargetLocation.GENERATED_RESOURCES;
}
