package de.ctoffer.commons.storage;

import lombok.Builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ContinuousRegister<T extends Identifiable<Integer>> extends Register<Integer, T> {

    private final List<T> internalStorage;
    private final List<Integer> freeIds;

    @Builder
    public ContinuousRegister(
            final String name,
            final Function<Integer, T> constructor,
            final StorageConcept<Integer, T> associatedStorage
    ) {
        super(name, constructor, associatedStorage);
        this.internalStorage = new ArrayList<>();
        this.freeIds = new ArrayList<>();
    }

    @Override
    protected Integer nextFreeId() {
        int result;
        if (freeIds.isEmpty()) {
            result = internalStorage.size();
        } else {
            result = freeIds.remove(0);
        }

        return result;
    }

    @Override
    protected void put(
            final Integer id,
            final T object
    ) {
        if (id >= internalStorage.size()) {
            internalStorage.add(id, object);
        } else {
            internalStorage.set(id, object);
        }
    }

    @Override
    protected T retrieve(
            final Integer id
    ) {
        return internalStorage.get(id);
    }

    public Optional<T> getObjectById(
            int id
    ) {
        Optional<T> result = Optional.empty();

        if (!freeIds.contains(id)) {
            result = Optional.of(loadObjectFromStorageIfNecessary(id));
        }

        return result;
    }

    @Override
    public Integer deleteObject(
            final T object
    ) {
        var id = super.deleteObject(object);
        freeIds.add(id);
        Collections.sort(freeIds);
        return id;
    }

    public int freeSpace() {
        return this.freeIds.size();
    }
}
