package de.ctoffer.commons.annotations.compile;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface SystemDService {

    String serviceName();
    int[] defaultStart();
    int[] defaultStop();
    String shortDescription();
    String description();
    String[] jvmArgs() default {"-Xmx100M"};
    String[] systemProperties() default {};
    String[] programArgs() default {};
}
