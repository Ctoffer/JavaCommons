package de.ctoffer.commons.io.pretty.components;

import de.ctoffer.commons.io.pretty.dto.RenderOptions;

import java.util.List;

public interface Component {
    List<String> render(RenderOptions options);
}
