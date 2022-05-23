package de.ctoffer.commons.annotations.processor.maven;

import de.ctoffer.commons.container.Pair;
import lombok.Getter;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class EnvironmentLookup extends HashMap<String, String> {

    public interface Namespace {
        String getName();

        Namespace insert(String key, String value);

        Namespace insert(Map<String, String> values);
    }

    public Namespace simpleNamespace(final String namespace, final String... aliases) {
        return new Namespace() {
            @Getter
            private final String name = namespace;
            private final String[] alternatives = aliases;

            @Override
            public Namespace insert(String key, String value) {
                EnvironmentLookup.this.put(getName() + ":" + key, value);

                for (final String alternative : alternatives) {
                    EnvironmentLookup.this.put(alternative + ":" + key, value);
                }

                return this;
            }

            @Override
            public Namespace insert(Map<String, String> values) {
                final Comparator<Pair<String, String>> comp = Comparator.comparing(Pair::getSecond);

                values.entrySet().stream().map(Pair::of).sorted(comp.reversed()).forEach(entry -> {
                    var value = entry.getSecond();

                    if (value.startsWith("${") && value.endsWith("}")) {
                        value = EnvironmentLookup.this.get(getName() + ":" + value.substring(2, value.length() - 1));
                    }

                    EnvironmentLookup.this.put(getName() + ":" + entry.getFirst(), value);

                    for(final String alternative: alternatives) {
                        EnvironmentLookup.this.put(alternative + ":" + entry.getFirst(), value);
                    }
                });

                return this;
            }
        };
    }
}
