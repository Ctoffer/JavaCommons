package de.ctoffer.commons.installer;

import de.ctoffer.commons.io.StdIo;

public class RpiServiceInstaller {
    public static void main(String[] args) {
        StdIo.print(System.getProperty("artifactId"), System.getProperty("version"));

        // Get pom.xml and MavenProjectStructure object
        // Use final var reader = new MavenXpp3Reader(); to read POM to retrieve artifact id
        // get all important scripts / artifacts and transform them with gzip -> base64
        // create shell script installer
    }
}
