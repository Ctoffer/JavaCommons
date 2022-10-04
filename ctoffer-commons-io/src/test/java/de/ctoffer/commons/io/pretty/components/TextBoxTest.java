package de.ctoffer.commons.io.pretty.components;

import de.ctoffer.commons.io.pretty.Style;
import de.ctoffer.commons.io.pretty.dto.Margins;
import de.ctoffer.commons.io.pretty.dto.Padding;
import de.ctoffer.commons.io.pretty.dto.RenderOptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TextBoxTest {

    @Test
    void testEmptyTextBox() {
        final var textBox = new TextBox("This", "is", "a", "test");

        var lines = textBox.render(
                RenderOptions.builder()
                        .margins(Margins.one())
                        .padding(Padding.one())
                        .maxWidth(10)
                        .showBorder(true)
                        .style(Style.singleLineStyle())
                        .build()
        );

        Assertions.assertAll(
                () -> Assertions.assertEquals("          ", lines.get(0)),
                () -> Assertions.assertEquals(" ┌──────┐ ", lines.get(1)),
                () -> Assertions.assertEquals(" │      │ ", lines.get(2)),
                () -> Assertions.assertEquals(" │ This │ ", lines.get(3)),
                () -> Assertions.assertEquals(" │ is   │ ", lines.get(4)),
                () -> Assertions.assertEquals(" │ a    │ ", lines.get(5)),
                () -> Assertions.assertEquals(" │ test │ ", lines.get(6)),
                () -> Assertions.assertEquals(" │      │ ", lines.get(7)),
                () -> Assertions.assertEquals(" └──────┘ ", lines.get(8)),
                () -> Assertions.assertEquals("          ", lines.get(9))
        );
    }
}