package de.ctoffer.commons.container.utils;

import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.function.Function;

@AllArgsConstructor
public class MapBuilder <K, V> {

    public static class MapBuilderConfiguration {

    }

    private final Map<K, V> temporaryMap;
    private final Function<Map<K, V>, Map<K, V>> wrapper;

    public MapBuilder<K, V> put(final K key, final V value) {
        temporaryMap.put(key, value);
        return this;
    }

    public static <K, V> Map<K, V> map() {
        return null;
    }
}
