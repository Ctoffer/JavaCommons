package de.ctoffer.commons.storage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.IntFunction;

public class Register<T extends Identifiable> {
    public static class RegisterException extends RuntimeException {
        public RegisterException(final String message) {
            super(message);
        }
    }

    private final String name;
    private final List<T> internalStorage;
    private final List<Integer> freeIds;

    private final IntFunction<T> constructor;

    private final StorageConcept<T> associatedStorage;

    public Register(final String name,
                    final IntFunction<T> constructor,
                    final StorageConcept<T> associatedStorage) {
        this.name = name;
        this.internalStorage = new ArrayList<>();
        this.freeIds = new ArrayList<>();

        this.constructor = constructor;
        this.associatedStorage = associatedStorage;
    }

    public T newObject() {
        int id = nextFreeId();
        T result = constructor.apply(id);
        addAndSaveToStorage(result);

        return result;
    }

    private int nextFreeId() {
        int result;
        if (freeIds.isEmpty()) {
            result = internalStorage.size();
        } else {
            result = freeIds.remove(0);
        }

        return result;
    }

    private void addAndSaveToStorage(final T object) {
        int index = object.id();

        if (index >= internalStorage.size()) {
            internalStorage.add(index, object);
        } else {
            internalStorage.set(index, object);
        }
        associatedStorage.save(object);
    }

    public Optional<T> getObjectById(int id) {
        Optional<T> result = Optional.empty();

        if (!freeIds.contains(id)) {
            result = Optional.of(loadObjectFromStorageIfNecessary(id));
        }

        return result;
    }

    private T loadObjectFromStorageIfNecessary(int id) {
        if (internalStorage.get(id) == null) {
            internalStorage.set(id, associatedStorage.load(id));
        }

        return internalStorage.get(id);
    }

    public void updateObject(final T object) {
        int id = object.id();
        if (object != internalStorage.get(object.id())) {
            throw new RegisterException("Given object with id " + id + " does not match already stored instance!");
        }
        associatedStorage.save(object);
    }

    public void deleteObject(final T object) {
        int id = newObject().id();
        associatedStorage.delete(object);
        internalStorage.set(id, null);
        freeIds.add(id);
        Collections.sort(freeIds);
    }

    public int usedSpace() {
        return totalSpace() - freeSpace();
    }

    public int totalSpace() {
        return this.internalStorage.size();
    }

    public int freeSpace() {
        return this.freeIds.size();
    }
}
