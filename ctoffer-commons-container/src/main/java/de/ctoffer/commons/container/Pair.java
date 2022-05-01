package de.ctoffer.commons.container;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class Pair<F, S> {

    public final F first;
    public final S second;
}
