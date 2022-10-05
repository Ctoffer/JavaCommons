package de.ctoffer.commons.io.pretty.components;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@RequiredArgsConstructor
public class TextBox extends Component {

    // TODO (Ctoffer): Add a Frame class which only does the outer-border around any List<String>

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
            final int maxWidth
    ) {

        final var emptyLine = StringUtils.repeat(' ', maxWidth);

        final var leftMargin = StringUtils.repeat(' ', margins.left());
        final var rightMargin = StringUtils.repeat(' ', margins.right());
        final var leftPadding = StringUtils.repeat(' ', padding.left());
        final var rightPadding = StringUtils.repeat(' ', padding.right());

        int paddedContentWidth = maxWidth - leftMargin.length() - rightMargin.length() - (visibility.left() ? 1 : 0) - (visibility.right() ? 1 : 0);
        int contentWidth = paddedContentWidth - leftPadding.length() - rightPadding.length();

        final var emptyPaddingLine = leftMargin
                + style.vertical()
                + StringUtils.repeat(' ', paddedContentWidth)
                + style.vertical()
                + rightMargin;

        final var result = new ArrayList<String>();

        IntStream.range(0, margins.top()).forEach(i -> result.add(emptyLine));
        if (visibility.top()) {
            result.add(
                    leftMargin
                            + (visibility.left() ? style.topLeft() : "")
                            + StringUtils.repeat(style.horizontal(), paddedContentWidth)
                            + (visibility.right() ? style.topRight() : "")
                            + rightMargin
            );
        }
        IntStream.range(0, padding.top()).forEach(i -> result.add(emptyPaddingLine));

        for (final var content : this.lines) {
            result.add(
                    leftMargin
                            + (visibility.left() ? style.vertical() : "")
                            + leftPadding
                            + StringUtils.rightPad(content, contentWidth)
                            + rightPadding
                            + (visibility.right() ? style.vertical() : "")
                            + rightMargin
            );
        }

        IntStream.range(0, padding.bot()).forEach(i -> result.add(emptyPaddingLine));
        if (visibility.bot()) {
            result.add(
                    leftMargin
                            + (visibility.left() ? style.botLeft() : "")
                            + StringUtils.repeat(style.horizontal(), paddedContentWidth)
                            + (visibility.right() ? style.botRight() : "")
                            + rightMargin
            );
        }
        IntStream.range(0, margins.bot()).forEach(i -> result.add(emptyLine));

        return result;
    }
}
