package de.ctoffer.commons.io.pretty.components;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Table extends Component {

    private final int[] columnWidth;
    private final List<String> columnHeaders;
    private final List<List<String>> rowData;

    @Accessors(fluent = true)
    @Getter
    @Setter
    private int columnPadding = 0;

    public <T> Table(
            final List<String> columnHeaders,
            final List<T> data,
            final BiFunction<String, T, String> mapToRow
    ) {
        this.columnHeaders = columnHeaders;
        this.columnWidth = new int[columnHeaders.size()];
        this.rowData = new ArrayList<>();

        for (final var element : data) {
            final var row = columnHeaders.stream()
                    .map(header -> mapToRow.apply(header, element))
                    .collect(Collectors.toList());
            rowData.add(row);
        }

        for (int i = 0; i < columnWidth.length; ++i) {
            columnWidth[i] = columnHeaders.get(i).length();
            for (final var rowDatum : rowData) {
                var dataLength = rowDatum.get(i).length();
                if (dataLength > columnWidth[i]) {
                    columnWidth[i] = dataLength;
                }
            }
        }
    }

    @Override
    protected int requestedContentWidth() {
        return IntStream.of(columnWidth).map(i -> 2 * columnPadding + i).sum() + columnWidth.length + 1;
    }

    @Override
    public List<String> render(int maxWidth) {
        int[] additionalWidth = new int[columnHeaders.size()];
        if (requestedWidth() < maxWidth) {
            additionalWidth = partition(maxWidth - requestedWidth() + 2, additionalWidth.length);
        }

        final var topDivider = createDivider(style.topLeft(), style.top(), style.topRight(), additionalWidth);
        final var divider = createDivider(style.left(), style.center(), style.right(), additionalWidth);
        final var botDivider = createDivider(style.botLeft(), style.bot(), style.botRight(), additionalWidth);

        List<String> result = new ArrayList<>();

        result.add(topDivider);
        result.add(createLine(columnHeaders, additionalWidth));
        result.add(divider);
        for (final var row : rowData) {
            result.add(createLine(row, additionalWidth));
        }
        result.add(botDivider);

        compressResultIfNecessary(result, maxWidth);

        return result;
    }

    private String createLine(
            final List<String> content,
            final int[] additionalWidth
    ) {
        final var columnPad = StringUtils.repeat(' ', columnPadding);
        final var contentBuilder = new ArrayList<String>();

        for (int i = 0; i < content.size(); ++i) {
            contentBuilder.add(
                    columnPad
                            + StringUtils.leftPad(content.get(i), columnWidth[i] + additionalWidth[i])
                            + columnPad
            );
        }

        return style.vertical()
                + String.join("" + style.vertical(), contentBuilder)
                + style.vertical();
    }

    private String createDivider(
            final char first,
            final char filler,
            final char last,
            final int[] additionalWidth
    ) {
        return first
                + IntStream.range(0, columnWidth.length)
                .mapToObj(i -> StringUtils.repeat(style.horizontal(), columnWidth[i] + additionalWidth[i]))
                .collect(Collectors.joining("" + filler))
                + last;
    }

    private int[] partition(int amount, int numberOfPartitions) {
        final int[] result = new int[numberOfPartitions];
        int toCopy = amount;
        int i = 0;
        while (toCopy > 0) {
            result[i] += 1;
            --toCopy;
            i = (++i) % result.length;
        }

        return result;
    }

    private void compressResultIfNecessary(final List<String> result, int maxWidth) {
        if (requestedWidth() - 2 > maxWidth) {
            var partitions = partition(maxWidth - 3, 2);

            for (int i = 0; i < result.size(); ++i) {
                var line = result.get(i);
                result.set(
                        i,
                        line.substring(0, partitions[0])
                                + "..."
                                + line.substring(line.length() - partitions[1])
                );
            }
        }
    }
}
