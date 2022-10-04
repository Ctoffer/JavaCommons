package de.ctoffer.commons.io.pretty.dto;

import lombok.Builder;


public record Padding(
        int top,
        int right,
        int bot,
        int left
) {
    @Builder public Padding {}

    public static Padding zero() {
        return singleValuePadding(0);
    }

    public static Padding singleValuePadding(int value) {
        return Padding.builder()
                .top(value)
                .right(value)
                .bot(value)
                .left(value)
                .build();
    }

    public static Padding one() {
        return singleValuePadding(1);
    }
}
