package de.ctoffer.commons.installer;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class RpiServiceInstallerTest {

    @Test
    void testBase64Text() {
        final String expected = "H4sIAAAAAAAAA1WMvw6CMByE9yZ9hwM7wNAinTTEd3AnDPxp5YekkIImanx3S3RxueHu+24XZQ25\n" +
                "rKmXnjPOZupOIsF88WaGtGjXyVrj5WOVi/F3ao3Ue63VQeVa5WqoPdLNI4uyRAT5RCxe4eUdo6oK\n" +
                "rL1xnAF28mipAzmI5Pd+xpdMC3QTlluIK40j5BEisFvrzCb/T0HhzBJnH6CNRTa7AAAA\n";

        var path = Paths.get("src", "test", "resources", "stop.sh");
        var actual = RpiServiceInstaller.base64Text(path);

        assertEquals(expected, actual);
    }

}