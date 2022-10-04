package de.ctoffer.commons.io.pretty.dto;

import lombok.Builder;

public record Margins(
        int top,
        int right,
        int bot,
        int left
) {
    @Builder public Margins {}

    public static Margins zero() {
        return singleValueMargin(0);
    }

    public static Margins singleValueMargin(int value) {
        return Margins.builder()
                .top(value)
                .right(value)
                .bot(value)
                .left(value)
                .build();
    }

    public static Margins one() {
        return singleValueMargin(1);
    }
}
