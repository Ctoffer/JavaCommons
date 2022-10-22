package de.ctoffer.commons.storage.impl;

import de.ctoffer.commons.storage.base.StorageGroup;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.nio.file.Path;

@RequiredArgsConstructor(access =  AccessLevel.PRIVATE)
public class StorageBuilder {

    private final Storage storage;

    public static StorageBuilder builder(final Path home) {
        return new StorageBuilder( Storage.openIn(home));
    }

    public <T> StorageBuilder registerGroup(final StorageGroup<T> group) {
        this.storage.registerGroup(group);
        return this;
    }

    public Storage build() {
        return this.storage;
    }
}
