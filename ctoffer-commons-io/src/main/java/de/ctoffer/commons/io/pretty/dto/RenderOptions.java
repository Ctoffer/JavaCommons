package de.ctoffer.commons.io.pretty.dto;

import de.ctoffer.commons.io.pretty.Style;
import lombok.Builder;

public record RenderOptions(
        Style style,
        int maxWidth,
        boolean showBorder,
        Margins margins,
        Padding padding
) {

    @Builder public RenderOptions {}
}
