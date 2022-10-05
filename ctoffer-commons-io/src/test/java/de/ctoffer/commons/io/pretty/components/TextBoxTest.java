package de.ctoffer.commons.io.pretty.components;

import de.ctoffer.commons.io.pretty.Style;
import de.ctoffer.commons.io.pretty.dto.BorderVisibility;
import de.ctoffer.commons.io.pretty.dto.Margins;
import de.ctoffer.commons.io.pretty.dto.Padding;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TextBoxTest {

    @Nested
    class TextBoxBorderTest {
        @Test
        void testTextBoxWithoutBorderText() {
            final var textBox = new TextBox("Test");

            textBox.visibility(BorderVisibility.allInvisible());

            var lines = textBox.render(10);

            Assertions.assertAll(
                    () -> Assertions.assertEquals(1, lines.size()),
                    () -> Assertions.assertEquals("Test      ", lines.get(0))
            );
        }

        @Test
        void testTextBoxWithoutLeftBorderText() {
            final var textBox = new TextBox("Test");

            textBox.visibility(BorderVisibility.builder().left(false).build());

            var lines = textBox.render(10);

            Assertions.assertAll(
                    () -> Assertions.assertEquals(3, lines.size()),
                    () -> Assertions.assertEquals("─────────┐", lines.get(0)),
                    () -> Assertions.assertEquals("Test     │", lines.get(1)),
                    () -> Assertions.assertEquals("─────────┘", lines.get(2))
            );
        }

        @Test
        void testTextBoxWithoutRightBorderText() {
            final var textBox = new TextBox("Test");

            textBox.visibility(BorderVisibility.builder().right(false).build());

            var lines = textBox.render(10);

            Assertions.assertAll(
                    () -> Assertions.assertEquals(3, lines.size()),
                    () -> Assertions.assertEquals("┌─────────", lines.get(0)),
                    () -> Assertions.assertEquals("│Test     ", lines.get(1)),
                    () -> Assertions.assertEquals("└─────────", lines.get(2))
            );
        }

        @Test
        void testTextBoxWithoutTopBorderText() {
            final var textBox = new TextBox("Test");

            textBox.visibility(BorderVisibility.builder().top(false).build());

            var lines = textBox.render(10);

            Assertions.assertAll(
                    () -> Assertions.assertEquals(2, lines.size()),
                    () -> Assertions.assertEquals("│Test    │", lines.get(0)),
                    () -> Assertions.assertEquals("└────────┘", lines.get(1))
            );
        }

        @Test
        void testTextBoxWithoutBotBorderText() {
            final var textBox = new TextBox("Test");

            textBox.visibility(BorderVisibility.builder().bot(false).build());

            var lines = textBox.render(10);

            Assertions.assertAll(
                    () -> Assertions.assertEquals(2, lines.size()),
                    () -> Assertions.assertEquals("┌────────┐", lines.get(0)),
                    () -> Assertions.assertEquals("│Test    │", lines.get(1))
            );
        }
    }



    @Test
    void testTextBoxWithMultilineText() {
        final var textBox = new TextBox("This", "is", "a", "test");

        var lines = textBox.render(10);

        Assertions.assertAll(
                () -> Assertions.assertEquals(6, lines.size()),
                () -> Assertions.assertEquals("┌────────┐", lines.get(0)),
                () -> Assertions.assertEquals("│This    │", lines.get(1)),
                () -> Assertions.assertEquals("│is      │", lines.get(2)),
                () -> Assertions.assertEquals("│a       │", lines.get(3)),
                () -> Assertions.assertEquals("│test    │", lines.get(4)),
                () -> Assertions.assertEquals("└────────┘", lines.get(5))
        );
    }

    @Test
    void testTextBoxPaddingAndMargins() {
        final var textBox = new TextBox("This", "is", "a", "test");

        textBox.margins(Margins.builder().top(1).right(2).bot(3).left(4).build());
        textBox.padding(Padding.builder().top(4).right(3).bot(2).left(1).build());

        var lines = textBox.render(20 );

        Assertions.assertAll(
                () -> Assertions.assertEquals(16, lines.size()),
                () -> Assertions.assertEquals("                    ", lines.get(0)),
                () -> Assertions.assertEquals("    ┌────────────┐  ", lines.get(1)),
                () -> Assertions.assertEquals("    │            │  ", lines.get(2)),
                () -> Assertions.assertEquals("    │            │  ", lines.get(3)),
                () -> Assertions.assertEquals("    │            │  ", lines.get(4)),
                () -> Assertions.assertEquals("    │            │  ", lines.get(5)),
                () -> Assertions.assertEquals("    │ This       │  ", lines.get(6)),
                () -> Assertions.assertEquals("    │ is         │  ", lines.get(7)),
                () -> Assertions.assertEquals("    │ a          │  ", lines.get(8)),
                () -> Assertions.assertEquals("    │ test       │  ", lines.get(9)),
                () -> Assertions.assertEquals("    │            │  ", lines.get(10)),
                () -> Assertions.assertEquals("    │            │  ", lines.get(11)),
                () -> Assertions.assertEquals("    └────────────┘  ", lines.get(12)),
                () -> Assertions.assertEquals("                    ", lines.get(13)),
                () -> Assertions.assertEquals("                    ", lines.get(14)),
                () -> Assertions.assertEquals("                    ", lines.get(15))
        );
    }
}