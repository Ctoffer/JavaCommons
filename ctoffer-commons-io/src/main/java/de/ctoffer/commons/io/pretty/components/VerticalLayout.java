package de.ctoffer.commons.io.pretty.components;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VerticalLayout extends Container {

    @Getter
    @Setter
    private boolean mergeBorders;

    public VerticalLayout(final Component... components) {
        this(Arrays.asList(components));
    }

    public VerticalLayout(final List<Component> components) {
        super(components);
    }

    @Override
    public List<String> render(int maxWidth) {
        final var result = new ArrayList<String>();
        int[] indices = new int[components.size()];

        if (!(components.isEmpty())) {
            result.addAll(components.get(0).render(maxWidth));
            indices[0] = result.size();

            for (int i = 1; i < components.size(); ++i) {
                var component = components.get(i);
                var topVisibility = component.topVisibility();

                if (mergeBorders) {
                    component.topVisibility(false);
                }

                result.addAll(component.render(maxWidth));
                indices[i] = result.size();
                component.topVisibility(topVisibility);
            }

            if (mergeBorders) {
                mergeAllLines(result, indices);
            }
        }

        return result;
    }

    private void mergeAllLines(final List<String> result, final int[] indices) {
        for (int i = 0; i < indices.length - 1; ++i) {
            int index = indices[i];
            var currentLine = result.get(index).toCharArray();
            var nextLine = result.get(index + 1).toCharArray();

            mergeAdjacentLines(currentLine, nextLine);

            result.set(i, new String(currentLine));
        }
    }

    private void mergeAdjacentLines(final char[] currentLine, final char[] nextLine) {
        for (int j = 0; j < currentLine.length; ++j) {
            if (currentLine[j] == style.botLeft() && nextLine[j] == style.vertical()) {
                currentLine[j] = style.left();
            } else if (currentLine[j] == style.botRight() && nextLine[j] == style.vertical()) {
                currentLine[j] = style.right();
            } else if (currentLine[j] == style.horizontal() && nextLine[j] == style.vertical()) {
                currentLine[j] = style.top();
            } else if (currentLine[j] == style.bot() && nextLine[j] == style.vertical()) {
                currentLine[j] = style.center();
            }
        }
    }
}
