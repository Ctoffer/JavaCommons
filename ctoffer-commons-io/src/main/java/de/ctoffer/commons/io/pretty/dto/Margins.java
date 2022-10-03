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
        return Margins.builder()
                .top(0)
                .right(0)
                .bot(0)
                .left(0)
                .build();
    }
}
