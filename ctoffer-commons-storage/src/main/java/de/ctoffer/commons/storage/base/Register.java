package de.ctoffer.commons.storage.base;

import de.ctoffer.commons.storage.api.Identifiable;
import de.ctoffer.commons.storage.api.StorageConcept;
import de.ctoffer.commons.storage.exception.RegisterException;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
public abstract class Register<I, T extends Identifiable<I>> {

    protected final String name;
    protected final Function<I, T> constructor;
    protected final StorageConcept<I, T> associatedStorage;

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

        return retrieve(id);
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

}
