package de.ctoffer.commons.io.pretty;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class StyleTest {

    @ParameterizedTest
    @ValueSource(strings = {"", "123456789012"})
    void testCreateStyleWithValidInput(final String input) {
        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> Style.createStyle(input)
        );
        Assertions.assertEquals(
                "Style requires exactly 11 symbols",
                exception.getLocalizedMessage()
        );
    }

    @Test
    void testCreateStyleWithValidInput() {
        final var style = Style.createStyle("│─┌┐└┘├┤┬┴┼");

        Assertions.assertAll(
                () -> Assertions.assertEquals('│', style.vertical()),
                () -> Assertions.assertEquals('─', style.horizontal()),

                () -> Assertions.assertEquals('┌', style.topLeft()),
                () -> Assertions.assertEquals('┐', style.topRight()),
                () -> Assertions.assertEquals('┘', style.botRight()),
                () -> Assertions.assertEquals('└', style.botLeft()),

                () -> Assertions.assertEquals('├', style.left()),
                () -> Assertions.assertEquals('┤', style.right()),
                () -> Assertions.assertEquals('┬', style.top()),
                () -> Assertions.assertEquals('┴', style.bot()),

                () -> Assertions.assertEquals('┼', style.center())
        );
    }
}