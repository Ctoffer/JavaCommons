package de.ctoffer.commons.installer;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class RpiServiceInstallerTest {

    @Test
    void testBase64Text() {
        final String expected =
                "IyEvYmluL2Jhc2gNCg0KcGlkPSQoIHBncmVwIC1mIGN0b2ZmZXIteXQtc2VydmljZS0yMDIyLjgu\n" +
                "MTIuMS5qYXIgKQ0KDQppZiBbWyAhIC16ICIke3BpZH0iIF1dOyB0aGVuDQogIGZvciBjaWQgaW4g\n" +
                "JChwZ3JlcCAtUCAke3BpZH0pOyBkbyBzdWRvIGtpbGwgLTkgJGNpZDsgZG9uZQ0KICBzdWRvIGtp\n" +
                "bGwgLTkgJHBpZA0KZmkNCg==";

        var path = Paths.get("src", "test", "resources", "stop.sh");
        var actual = RpiServiceInstaller.base64(path);

        assertEquals(expected, actual);
    }

}