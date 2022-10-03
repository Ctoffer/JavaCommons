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
        return Padding.builder()
                .top(0)
                .right(0)
                .bot(0)
                .left(0)
                .build();
    }
}
