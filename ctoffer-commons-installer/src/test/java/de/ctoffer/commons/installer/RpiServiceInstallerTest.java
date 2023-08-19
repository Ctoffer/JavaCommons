package de.ctoffer.commons.installer;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class RpiServiceInstallerTest {

    @Test
    void testBase64Text() {
        final String expected =
                "IyEvYmluL2Jhc2gKCnBpZD0kKCBwZ3JlcCAtZiBjdG9mZmVyLXl0LXNlcnZpY2UtMjAyMi44LjEy\n" +
                "LjEuamFyICkKCmlmIFtbICEgLXogIiR7cGlkfSIgXV07IHRoZW4KICBmb3IgY2lkIGluICQocGdy\n" +
                "ZXAgLVAgJHtwaWR9KTsgZG8gc3VkbyBraWxsIC05ICRjaWQ7IGRvbmUKICBzdWRvIGtpbGwgLTkg\n" +
                "JHBpZApmaQo=";

        var path = Paths.get("src", "test", "resources", "stop.sh");
        var actual = RpiServiceInstaller.base64(path);

        assertEquals(expected, actual);
    }

}