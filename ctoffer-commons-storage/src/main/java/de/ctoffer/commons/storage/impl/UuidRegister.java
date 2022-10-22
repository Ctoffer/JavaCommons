package de.ctoffer.commons.storage.impl;

import de.ctoffer.commons.storage.api.Identifiable;
import de.ctoffer.commons.storage.api.StorageConcept;
import de.ctoffer.commons.storage.base.Register;
import de.ctoffer.commons.storage.exception.RegisterException;
import lombok.Builder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class UuidRegister extends Register<String, Identifiable<String>> {

    private final Map<String, Identifiable<String>> internalStorage = new HashMap<>();

    @Builder
    public UuidRegister(
            final String name,
            final Function<String, Identifiable<String>> constructor,
            final StorageConcept<String, Identifiable<String>> associatedStorage
    ) {
        super(name, constructor, associatedStorage);
    }

    @Override
    protected String nextFreeId() {
        return UUID.randomUUID().toString();
    }

    @Override
    protected void put(
            final String id,
            final Identifiable<String> object
    ) {
        internalStorage.put(id, object);
    }

    @Override
    protected Identifiable<String> retrieve(
            final String id
    ) {
        if (!(internalStorage.containsKey(id))) {
            throw new RegisterException("No object with " + id + " found.");
        }

        return  internalStorage.get(id);
    }
}
