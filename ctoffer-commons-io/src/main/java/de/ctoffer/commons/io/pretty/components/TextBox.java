package de.ctoffer.commons.io.pretty.components;

import de.ctoffer.commons.io.pretty.dto.RenderOptions;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class TextBox implements Component {

    private final List<String> lines;

    public TextBox(
            final String text,
            final String... followupText
    ) {
        this(consolidate(text, followupText));
    }

    private static List<String> consolidate(
            final String text,
            final String... followupText
    ) {
        final var lines = new ArrayList<String>();
        lines.add(text);
        lines.addAll(Arrays.asList(followupText));
        return lines;
    }

    @Override
    public List<String> render(
            final RenderOptions options
    ) {
        int maxWidth = options.maxWidth();

        return null;
    }
}
