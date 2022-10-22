package de.ctoffer.commons.storage;

import de.ctoffer.commons.storage.api.Identifiable;
import de.ctoffer.commons.storage.api.StorageConcept;
import de.ctoffer.commons.storage.impl.ContinuousRegister;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class ContinuousRegisterTest {

    @RequiredArgsConstructor
    @EqualsAndHashCode
    @ToString
    private class InMemoryObject implements Identifiable<Integer> {

        private final int id;

        @Override
        public Integer id() {
            return this.id;
        }
    }

    private class InMemoryStorage implements StorageConcept<Integer, InMemoryObject> {

        private final Map<Integer, InMemoryObject> underlyingStorage = new HashMap<>();

        @Override
        public void delete(final InMemoryObject object) {
            this.underlyingStorage.remove(object);
        }

        @Override
        public InMemoryObject load(final Integer id) {
            return underlyingStorage.get(id);
        }

        @Override
        public void save(final InMemoryObject object) {
            this.underlyingStorage.put(object.id(), object);
        }
    }

    private final InMemoryStorage storage = new InMemoryStorage();

    @Test
    void testNewObject() {
        final var register = ContinuousRegister.<InMemoryObject>builder()
                .name("")
                .constructor(InMemoryObject::new)
                .associatedStorage(storage)
                .build();

        var object = register.newObject();

        Assertions.assertEquals(0, object.id());
    }

}