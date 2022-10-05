package de.ctoffer.commons.io.pretty.components;

import de.ctoffer.commons.io.pretty.dto.BorderVisibility;
import de.ctoffer.commons.io.pretty.dto.Margins;
import de.ctoffer.commons.io.pretty.dto.Padding;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FrameTest {

    @Nested
    class FrameBorderTest {
        @Test
        void testFrameWithoutBorderText() {
            final var Frame = new Frame("Test");

            Frame.visibility(BorderVisibility.allInvisible());

            var lines = Frame.render(10);

            Assertions.assertAll(
                    () -> Assertions.assertEquals(1, lines.size()),
                    () -> Assertions.assertEquals("Test      ", lines.get(0))
            );
        }

        @Test
        void testFrameWithoutLeftBorderText() {
            final var Frame = new Frame("Test");

            Frame.visibility(BorderVisibility.builder().left(false).build());

            var lines = Frame.render(10);

            Assertions.assertAll(
                    () -> Assertions.assertEquals(3, lines.size()),
                    () -> Assertions.assertEquals("─────────┐", lines.get(0)),
                    () -> Assertions.assertEquals("Test     │", lines.get(1)),
                    () -> Assertions.assertEquals("─────────┘", lines.get(2))
            );
        }

        @Test
        void testFrameWithoutRightBorderText() {
            final var Frame = new Frame("Test");

            Frame.visibility(BorderVisibility.builder().right(false).build());

            var lines = Frame.render(10);

            Assertions.assertAll(
                    () -> Assertions.assertEquals(3, lines.size()),
                    () -> Assertions.assertEquals("┌─────────", lines.get(0)),
                    () -> Assertions.assertEquals("│Test     ", lines.get(1)),
                    () -> Assertions.assertEquals("└─────────", lines.get(2))
            );
        }

        @Test
        void testFrameWithoutTopBorderText() {
            final var Frame = new Frame("Test");

            Frame.visibility(BorderVisibility.builder().top(false).build());

            var lines = Frame.render(10);

            Assertions.assertAll(
                    () -> Assertions.assertEquals(2, lines.size()),
                    () -> Assertions.assertEquals("│Test    │", lines.get(0)),
                    () -> Assertions.assertEquals("└────────┘", lines.get(1))
            );
        }

        @Test
        void testFrameWithoutBotBorderText() {
            final var Frame = new Frame("Test");

            Frame.visibility(BorderVisibility.builder().bot(false).build());

            var lines = Frame.render(10);

            Assertions.assertAll(
                    () -> Assertions.assertEquals(2, lines.size()),
                    () -> Assertions.assertEquals("┌────────┐", lines.get(0)),
                    () -> Assertions.assertEquals("│Test    │", lines.get(1))
            );
        }
    }



    @Test
    void testFrameWithMultilineText() {
        final var Frame = new Frame("This", "is", "a", "test");

        var lines = Frame.render(10);

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
    void testFramePaddingAndMargins() {
        final var Frame = new Frame("This", "is", "a", "test");

        Frame.padding(Padding.builder().top(4).right(3).bot(2).left(1).build());

        var lines = Frame.render(14 );

        Assertions.assertAll(
                () -> Assertions.assertEquals(12, lines.size()),
                () -> Assertions.assertEquals("┌────────────┐", lines.get(0)),
                () -> Assertions.assertEquals("│            │", lines.get(1)),
                () -> Assertions.assertEquals("│            │", lines.get(2)),
                () -> Assertions.assertEquals("│            │", lines.get(3)),
                () -> Assertions.assertEquals("│            │", lines.get(4)),
                () -> Assertions.assertEquals("│ This       │", lines.get(5)),
                () -> Assertions.assertEquals("│ is         │", lines.get(6)),
                () -> Assertions.assertEquals("│ a          │", lines.get(7)),
                () -> Assertions.assertEquals("│ test       │", lines.get(8)),
                () -> Assertions.assertEquals("│            │", lines.get(9)),
                () -> Assertions.assertEquals("│            │", lines.get(10)),
                () -> Assertions.assertEquals("└────────────┘", lines.get(11))
        );
    }
}