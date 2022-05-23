package de.ctoffer.commons.container;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class Pair<F, S> {

    public final F first;
    public final S second;

    public <A, B> Pair<A, B> map(final Function<F, A> firstMapper, final Function<S, B> secondMapper) {
        return new Pair<>(firstMapper.apply(first), secondMapper.apply(second));
    }

    public static <F, S> Pair<F, S> of(final F first, final S second) {
        return new Pair<>(first, second);
    }

    public static <F, S> Pair<F, S> of(final Map.Entry<F, S> entry) {
        return of(entry.getKey(), entry.getValue());
    }

    public static <A, B, C, D> Function<Pair<A, B>, Pair<C, D>> mapper(
            final Function<A, C> firstMapper,
            final Function<B, D> secondMapper
    ) {
        return pair -> pair.map(firstMapper, secondMapper);
    }
}
