package de.ctoffer.commons.maven;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.nio.file.Path;

@Getter
@Accessors(fluent = true)
public class MavenProjectStructure {

    private final Path projectRoot;
    private final Path source;

    private final Path main;
    private final Path mainJava;
    private final Path mainResources;

    private final Path test;
    private final Path testJava;
    private final Path testResources;

    private final Path target;
    private final Path generatedResources;

    private final Path pom;

    public MavenProjectStructure(final Path projectRoot) {
        this.projectRoot = projectRoot;
        this.pom = this.projectRoot.resolve("pom.xml");
        this.source = this.projectRoot.resolve("src");

        this.main = this.source.resolve("main");
        this.mainJava = this.main.resolve("java");
        this.mainResources = this.main.resolve("resources");

        this.test = this.source.resolve("test");
        this.testJava = this.test.resolve("java");
        this.testResources = this.test.resolve("resources");

        this.target = this.projectRoot.resolve("target");
        this.generatedResources = this.target.resolve("generated-resources");
    }
}
