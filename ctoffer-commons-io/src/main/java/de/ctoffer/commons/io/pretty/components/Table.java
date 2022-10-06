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
        final var topDivider = createDivider(style.topLeft(), style.top(), style.topRight());
        final var divider = createDivider(style.left(), style.center(), style.right());
        final var botDivider = createDivider(style.botLeft(), style.bot(), style.botRight());

        List<String> result = new ArrayList<>();

        return result;
    }

    private String createDivider(final char first, final char filler, final char last) {
        return first
                + IntStream.of(columnWidth)
                .mapToObj(i -> StringUtils.repeat(style.horizontal(), i))
                .collect(Collectors.joining("" + filler))
                + last;
    }
}
