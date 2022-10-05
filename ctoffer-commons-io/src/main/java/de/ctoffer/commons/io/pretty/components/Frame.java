package de.ctoffer.commons.io.pretty.components;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@RequiredArgsConstructor
public class Frame extends Component {

    private final List<String> lines;
    private int maxSize = -1;

    public Frame(
            final String... followupText
    ) {
        this(Arrays.asList(followupText));
    }

    @Override
    protected int requestedContentWidth() {
        if (maxSize == -1) {
            maxSize = lines.stream()
                    .mapToInt(String::length)
                    .max()
                    .orElse(0);
        }

        return maxSize;
    }

    @Override
    public List<String> render(
            final int maxWidth
    ) {
        final var leftPadding = StringUtils.repeat(' ', padding.left());
        final var rightPadding = StringUtils.repeat(' ', padding.right());

        int paddedContentWidth = maxWidth - (visibility.left() ? 1 : 0) - (visibility.right() ? 1 : 0);
        int contentWidth = paddedContentWidth - leftPadding.length() - rightPadding.length();

        final var emptyPaddingLine =
                style.vertical()
                        + StringUtils.repeat(' ', paddedContentWidth)
                        + style.vertical();

        final var result = new ArrayList<String>();

        if (visibility.top()) {
            result.add(
                    (visibility.left() ? style.topLeft() : "")
                            + StringUtils.repeat(style.horizontal(), paddedContentWidth)
                            + (visibility.right() ? style.topRight() : "")
            );
        }
        IntStream.range(0, padding.top()).forEach(i -> result.add(emptyPaddingLine));

        for (final var content : this.lines) {
            result.add((visibility.left() ? style.vertical() : "")
                    + leftPadding
                    + StringUtils.rightPad(content, contentWidth)
                    + rightPadding
                    + (visibility.right() ? style.vertical() : "")
            );
        }

        IntStream.range(0, padding.bot()).forEach(i -> result.add(emptyPaddingLine));
        if (visibility.bot()) {
            result.add((visibility.left() ? style.botLeft() : "")
                    + StringUtils.repeat(style.horizontal(), paddedContentWidth)
                    + (visibility.right() ? style.botRight() : "")
            );
        }

        return result;
    }
}
