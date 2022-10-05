package de.ctoffer.commons.io.pretty.components;

import de.ctoffer.commons.io.pretty.Style;
import de.ctoffer.commons.io.pretty.dto.BorderVisibility;
import de.ctoffer.commons.io.pretty.dto.Margins;
import de.ctoffer.commons.io.pretty.dto.Padding;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(fluent = true)
@Getter
@Setter
public abstract class Component {

    protected Style style;
    protected Padding padding;
    protected BorderVisibility visibility;

    protected Component() {
        this.style = Style.singleLineStyle();
        this.padding = Padding.zero();
        this.visibility = BorderVisibility.allVisible();
    }

    public abstract List<String> render(int maxWidth);

}
