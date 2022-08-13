package de.ctoffer.commons.storage;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RegisterTest {

    @RequiredArgsConstructor
    @EqualsAndHashCode
    @ToString
    private class InMemoryObject implements Identifiable {

        private final int id;

        @Override
        public int id() {
            return this.id;
        }
    }

    private class InMemoryStorage implements StorageConcept<InMemoryObject> {

        private final Map<Integer, InMemoryObject> underlyingStorage = new HashMap<>();

        @Override
        public void delete(final InMemoryObject object) {
            this.underlyingStorage.remove(object);
        }

        @Override
        public InMemoryObject load(final int id) {
            return underlyingStorage.get(id);
        }

        @Override
        public void save(final InMemoryObject object) {
            this.underlyingStorage.put(object.id(), object);
        }
    }

    private InMemoryStorage storage = new InMemoryStorage();

    @Test
    void testNewObject() {
        final var register = new Register<InMemoryObject>("", InMemoryObject::new, storage);

        var object = register.newObject();

        Assertions.assertEquals(0, object.id());
    }

}