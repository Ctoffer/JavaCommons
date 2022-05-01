package de.ctoffer.commons.container;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class IntPair {

    public final int first;
    public final int second;

}
