package de.ctoffer.commons.io.pretty.components;

import de.ctoffer.commons.io.pretty.Style;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public abstract class Container extends Component {

    protected final List<Component> components;
    private int maxWidth = -1;

    @Override
    public Container style(final Style style) {
        this.style = style;
        components.forEach(component -> component.style(style));
        return this;
    }

    @Override
    protected int requestedContentWidth() {
        if (maxWidth == -1) {
            maxWidth = components.stream()
                    .mapToInt(Component::requestedContentWidth)
                    .max()
                    .orElse(0);
        }

        return maxWidth;
    }
}
