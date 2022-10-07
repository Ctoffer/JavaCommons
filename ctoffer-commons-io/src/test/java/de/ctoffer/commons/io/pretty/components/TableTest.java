package de.ctoffer.commons.io.pretty.components;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TableTest {
    @Test
    void testTableWithDefaultParameters() {
        final var header1 = "Header 1";
        final var header2 = "#2";
        final var header3 = "Attribute";

        var header = List.of(header1, header2, header3);
        var data = List.of(
                Map.of(header1, "#1", header2, "attribute", header3, ""),
                Map.of(header1, "#2", header2, "attr", header3, "Value"),
                Map.of(header1, "#3", header2, "A longer entry", header3, 123)
        );

        var underTest = new Table(header, data, (h, row) -> String.valueOf(row.get(h)));

        var result = underTest.render(35);

        assertAll(
                () -> assertEquals(7, result.size()),
                () -> assertEquals("┌────────┬──────────────┬─────────┐", result.get(0)),
                () -> assertEquals("│Header 1│            #2│Attribute│", result.get(1)),
                () -> assertEquals("├────────┼──────────────┼─────────┤", result.get(2)),
                () -> assertEquals("│      #1│     attribute│         │", result.get(3)),
                () -> assertEquals("│      #2│          attr│    Value│", result.get(4)),
                () -> assertEquals("│      #3│A longer entry│      123│", result.get(5)),
                () -> assertEquals("└────────┴──────────────┴─────────┘", result.get(6))
        );
    }

    @Test
    void testTableWithDefaultParametersAndTooLessSpace() {
        final var header1 = "Header 1";
        final var header2 = "#2";
        final var header3 = "Attribute";

        var header = List.of(header1, header2, header3);
        var data = List.of(
                Map.of(header1, "#1", header2, "attribute", header3, ""),
                Map.of(header1, "#2", header2, "attr", header3, "Value"),
                Map.of(header1, "#3", header2, "A longer entry", header3, 123)
        );

        var underTest = new Table(header, data, (h, row) -> String.valueOf(row.get(h)));

        var result = underTest.render(14);

        assertAll(
                () -> assertEquals(7, result.size()),
                () -> assertEquals("┌─────...────┐", result.get(0)),
                () -> assertEquals("│Heade...bute│", result.get(1)),
                () -> assertEquals("├─────...────┤", result.get(2)),
                () -> assertEquals("│     ...    │", result.get(3)),
                () -> assertEquals("│     ...alue│", result.get(4)),
                () -> assertEquals("│     ... 123│", result.get(5)),
                () -> assertEquals("└─────...────┘", result.get(6))
        );
    }

    @Test
    void testTableWithDefaultParametersAndTooMuchSpace() {
        final var header1 = "Header 1";
        final var header2 = "#2";
        final var header3 = "Attribute";

        var header = List.of(header1, header2, header3);
        var data = List.of(
                Map.of(header1, "#1", header2, "attribute", header3, ""),
                Map.of(header1, "#2", header2, "attr", header3, "Value"),
                Map.of(header1, "#3", header2, "A longer entry", header3, 123)
        );

        var underTest = new Table(header, data, (h, row) -> String.valueOf(row.get(h)));

        var result = underTest.render(40);

        assertAll(
                () -> assertEquals(7, result.size()),
                () -> assertEquals("┌──────────┬────────────────┬──────────┐", result.get(0)),
                () -> assertEquals("│  Header 1│              #2│ Attribute│", result.get(1)),
                () -> assertEquals("├──────────┼────────────────┼──────────┤", result.get(2)),
                () -> assertEquals("│        #1│       attribute│          │", result.get(3)),
                () -> assertEquals("│        #2│            attr│     Value│", result.get(4)),
                () -> assertEquals("│        #3│  A longer entry│       123│", result.get(5)),
                () -> assertEquals("└──────────┴────────────────┴──────────┘", result.get(6))
        );
    }
}