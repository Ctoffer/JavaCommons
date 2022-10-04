package de.ctoffer.commons.io.pretty.components;

import de.ctoffer.commons.io.pretty.dto.RenderOptions;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

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
        var style = options.style();
        var maxWidth = options.maxWidth();

        final var emptyLine = StringUtils.repeat(' ', maxWidth);

        final var leftMargin = StringUtils.repeat(' ', options.margins().left());
        final var rightMargin = StringUtils.repeat(' ', options.margins().right());
        final var leftPadding = StringUtils.repeat(' ', options.padding().left());
        final var rightPadding = StringUtils.repeat(' ', options.padding().right());

        int contentWidth = maxWidth - leftMargin.length() - rightMargin.length() - leftPadding.length() - rightMargin.length() - (options.showBorder() ? 2 : 0);
        final var emptyPaddingLine = leftMargin
                + style.vertical()
                + StringUtils.repeat(' ', contentWidth)
                + style.vertical()
                + rightMargin;

        final var result = new ArrayList<String>();

        IntStream.range(0, options.margins().top()).forEach(i -> result.add(emptyLine));
        result.add(
                leftMargin
                        + style.topLeft()
                        + StringUtils.repeat(style.horizontal(), contentWidth)
                        + style.topRight()
                        + rightMargin
        );
        IntStream.range(0, options.padding().top()).forEach(i -> result.add(emptyPaddingLine));

        for (final var content : this.lines) {
            result.add(
                    leftMargin + options.style().vertical() + leftPadding
                            + content
                            + rightPadding + options.style().vertical() + rightMargin
            );
        }

        IntStream.range(0, options.padding().bot()).forEach(i -> result.add(emptyPaddingLine));
        result.add(
                leftMargin
                        + style.botLeft()
                        + StringUtils.repeat(style.horizontal(), contentWidth)
                        + style.botRight()
                        + rightMargin
        );
        IntStream.range(0, options.margins().bot()).forEach(i -> result.add(emptyLine));

        return result;
    }
}
