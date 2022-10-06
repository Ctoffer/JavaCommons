package de.ctoffer.commons.io.pretty.components;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VerticalLayoutTest {

    @Test
    void testSeparatedLayout() {
        var underTest = new VerticalLayout(
                new Frame(" "),
                new Frame(" ")
        );

        var actual = underTest.render(3);

        Assertions.assertAll(
                () -> Assertions.assertEquals(6, actual.size()),
                () -> Assertions.assertEquals("┌─┐", actual.get(0)),
                () -> Assertions.assertEquals("│ │", actual.get(1)),
                () -> Assertions.assertEquals("└─┘", actual.get(2)),
                () -> Assertions.assertEquals("┌─┐", actual.get(3)),
                () -> Assertions.assertEquals("│ │", actual.get(4)),
                () -> Assertions.assertEquals("└─┘", actual.get(5))
        );
    }

    @Test
    void testMergedLayout() {
        var underTest = new VerticalLayout(
                new Frame(" "),
                new Frame(" ")
        );
        underTest.setMergeBorders(true);

        var actual = underTest.render(3);

        Assertions.assertAll(
                () -> Assertions.assertEquals(5, actual.size()),
                () -> Assertions.assertEquals("┌─┐", actual.get(0)),
                () -> Assertions.assertEquals("│ │", actual.get(1)),
                () -> Assertions.assertEquals("├─┤", actual.get(2)),
                () -> Assertions.assertEquals("│ │", actual.get(3)),
                () -> Assertions.assertEquals("└─┘", actual.get(4))
        );
    }
}