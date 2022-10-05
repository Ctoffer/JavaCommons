package de.ctoffer.commons.io.pretty.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Data
@Builder
public class BorderVisibility  {

    @Builder.Default
    private boolean top = true;

    @Builder.Default
    private boolean right = true;

    @Builder.Default
    private boolean bot = true;

    @Builder.Default
    private boolean left = true;


    public static BorderVisibility allVisible() {
        return BorderVisibility.builder().build();
    }

    public static BorderVisibility allInvisible() {
        return BorderVisibility.builder()
                .top(false)
                .right(false)
                .bot(false)
                .left(false)
                .build();
    }
}
