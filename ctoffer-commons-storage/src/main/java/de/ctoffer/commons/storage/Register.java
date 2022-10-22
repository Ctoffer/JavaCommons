package de.ctoffer.commons.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public abstract class Register<I, T extends Identifiable<I>> {
    public static class RegisterException extends RuntimeException {
        public RegisterException(final String message) {
            super(message);
        }
    }

    protected final String name;
    protected final Function<I, T> constructor;
    protected final StorageConcept<I, T> associatedStorage;

    protected final Map<String, T> internalStorage;

    public Register(
            final String name,
            final Function<I, T> constructor,
            final StorageConcept<I, T> associatedStorage
    ) {
        this.name = name;
        this.internalStorage = new HashMap<>();

        this.constructor = constructor;
        this.associatedStorage = associatedStorage;
    }


    public T newObject() {
        I id = nextFreeId();
        T result = constructor.apply(id);
        addAndSaveToStorage(result);

        return result;
    }

    protected abstract I nextFreeId();

    private void addAndSaveToStorage(final T object) {
        var id = object.id();
        put(id, object);
        associatedStorage.save(object);
    }

    protected abstract void put(I id, T object);

    public Optional<T> getObjectById(
            final I id
    ) {
        return Optional.of(loadObjectFromStorageIfNecessary(id));
    }

    protected T loadObjectFromStorageIfNecessary(
            final I id
    ) {
        if (retrieve(id) == null) {
            put(id, associatedStorage.load(id));
        }

        return internalStorage.get(id);
    }

    protected abstract T retrieve(I id);

    public void updateObject(final T object) {
        var id = object.id();
        if (object != retrieve(object.id())) {
            throw new RegisterException("Given object with id " + id + " does not match already stored instance!");
        }
        associatedStorage.save(object);
    }

    public I deleteObject(final T object) {
        var id = newObject().id();
        associatedStorage.delete(object);
        put(id, null);
        return id;
    }

    public int totalSpace() {
        return this.internalStorage.size();
    }

}
