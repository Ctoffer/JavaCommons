package de.ctoffer.commons.io.pretty.components;

import de.ctoffer.commons.io.pretty.Style;
import de.ctoffer.commons.io.pretty.dto.Margins;
import de.ctoffer.commons.io.pretty.dto.Padding;
import de.ctoffer.commons.io.pretty.dto.RenderOptions;
import org.junit.jupiter.api.Test;

class TextBoxTest {

    @Test
    void testEmptyTextBox() {
        final var textBox = new TextBox("This", "is", "a", "test");

        textBox.render(
                RenderOptions.builder()
                        .margins(Margins.zero())
                        .padding(Padding.zero())
                        .maxWidth(10)
                        .showBorder(true)
                        .style(Style.singleLineStyle())
                        .build()
        );
    }
}