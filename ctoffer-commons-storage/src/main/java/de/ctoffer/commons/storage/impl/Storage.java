package de.ctoffer.commons.storage.impl;

import de.ctoffer.commons.container.Pair;
import de.ctoffer.commons.container.SerializablePair;
import de.ctoffer.commons.storage.base.StorageGroup;
import de.ctoffer.commons.storage.exception.StorageException;
import de.ctoffer.commons.storage.util.Serial;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.nio.file.Files.walk;
import static java.util.Collections.unmodifiableList;
import static java.util.Comparator.*;
import static java.util.stream.Collectors.toList;

public class Storage {

    private static final Map<Path, Storage> STORAGES = new HashMap<>();
    public static final String OBJECT_NAMES = "__object_names";

    private final Path home;
    private final HashMap<Class<?>, StorageGroup<?>> groups;
    private final HashMap<Class<?>, Set<String>> objectNames;

    public static Storage openIn(final Path home) {
        STORAGES.computeIfAbsent(home, Storage::new);
        return STORAGES.get(home);
    }

    private Storage(
            final Path home
    ) {
        this.home = home;
        this.groups = new HashMap<>();
        this.objectNames = new HashMap<>();

        ensureHomeExists();
        loadDataIfExists();
    }

    private void ensureHomeExists() {
        try {
            createHomeIfNotExists();
        } catch (IOException e) {
            throw new StorageException(e);
        }
    }

    private void createHomeIfNotExists() throws IOException {
        if (Files.notExists(home)) {
            Files.createDirectory(home);
        }
    }

    private void loadDataIfExists() {
        final var namesPath = home.resolve(OBJECT_NAMES);
        if (Files.exists(namesPath)) {
            loadData();
        }
    }

    private void loadData() {
        loadNamesForAllStoredClasses();
    }

    private void loadNamesForAllStoredClasses() {
        final var names = home.resolve(OBJECT_NAMES);
        try (final Stream<Path> stream = Files.list(names)) {
            stream.forEach(path -> {
                final SerializablePair<Class<?>, HashSet<String>> pair = Serial.read(path);
                objectNames.put(pair.first, pair.second);
            });
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public <T> void registerGroup(StorageGroup<T> group) {
        Class<?> cls = group.classOfStoredObjects();
        groups.computeIfAbsent(cls, c -> group);
        objectNames.computeIfAbsent(cls, c -> new HashSet<>());
    }

    public void write(final Object o) {
        if (groups.containsKey(o.getClass())) {
            internalWrite(o);
        } else {
            throw new StorageException("No registered group for objects of type '" + o.getClass() + "'");
        }
    }

    @SuppressWarnings("unchecked")
    private <T> void internalWrite(final T o) {
        final Class<?> cls = o.getClass();
        final StorageGroup<T> group = (StorageGroup<T>) groups.get(cls);
        final String fileName = group.saveIn(home, o);
        objectNames.get(cls).add(fileName);
        updateMetaData();
    }

    private void updateMetaData() {
        for (final var entry : objectNames.entrySet()) {

            final var pair = Pair.of(entry).map(Function.identity(), HashSet::new);
            final var group = groups.get(pair.first);
            final var fName = group.name() + ".ser";
            final var result = home.resolve(OBJECT_NAMES).resolve(fName);

            Serial.write(
                    result,
                    SerializablePair.of(pair)
            );
        }
    }

    public List<String> getWrittenNames(final Class<?> cls) {
        final var set = objectNames.getOrDefault(cls, new HashSet<>());
        final var list = new ArrayList<>(set);
        list.sort(naturalOrder());
        return unmodifiableList(list);
    }

    public <T> Optional<T> read(
            final Class<? extends T> cls,
            final String name
    ) {
        final Set<String> names = objectNames.get(cls);
        Optional<T> result = Optional.empty();

        if (names != null && names.contains(name)) {
            result = internalRead(cls, name);
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    private <T> Optional<T> internalRead(
            final Class<? extends T> cls,
            final String name
    ) {
        final StorageGroup<T> group = (StorageGroup<T>) groups.get(cls);
        return group.loadFrom(home, name);
    }

    public <T> void delete(T obj) {
        try {
            internalDelete(obj);
        } catch (IOException e) {
            // silent catch
        }
    }

    @SuppressWarnings("unchecked")
    private <T> void internalDelete(
            final T obj
    ) throws IOException {
        final Class<? extends T> cls = (Class<? extends T>) obj.getClass();
        final StorageGroup<T> group = (StorageGroup<T>) groups.get(cls);
        final String name = group.createFileNameFor(obj);
        objectNames.get(cls).remove(name);
        final Path file = group.getDirectory(home).resolve(name);
        Files.delete(file);
        updateMetaData();
    }

    public Optional<Storage> copyStorageTo(
            final Path newHome
    ) {
        if (spaceAlreadyOccupied(newHome)) {
            return Optional.empty();
        }

        return copyToUnoccupiedSpace(newHome);
    }

    private boolean spaceAlreadyOccupied(
            final Path p
    ) {
        return home.equals(p) || STORAGES.containsKey(p);
    }

    private Optional<Storage> copyToUnoccupiedSpace(
            final Path newHome
    ) {
        Storage copy = openIn(newHome);
        try {
            copyDataFromThisTo(copy);
            return Optional.of(copy);
        } catch (Exception e) {
            copy.deleteStorage();
            return Optional.empty();
        }
    }

    private void copyDataFromThisTo(
            final Storage g2
    ) {
        copyGroups(g2);
        copyNames(g2);
        copyFiles(g2);
        g2.updateMetaData();
    }

    private void copyGroups(
            final Storage g2
    ) {
        for (final var entry : groups.entrySet()) {
            Class<?> cls = entry.getKey();
            StorageGroup<?> grp = entry.getValue();
            g2.groups.put(cls, grp);
        }
    }

    private void copyNames(
            final Storage g2
    ) {
        for (Map.Entry<Class<?>, Set<String>> entry : objectNames.entrySet()) {
            Class<?> cls = entry.getKey();
            Set<String> names = entry.getValue();
            g2.objectNames.put(cls, new HashSet<>(names));
        }
    }

    private void copyFiles(
            final Storage g2
    ) {
        final Path srcRoot = home;
        final Predicate<Path> isNotRoot = file -> !file.equals(srcRoot);

        try (final Stream<Path> stream = walk(home)) {
            stream.filter(isNotRoot).forEach(src -> {
                try {
                    final Path desitnation = g2.home.resolve(srcRoot.relativize(src));
                    Files.createDirectory(desitnation);
                    if (!Files.isDirectory(desitnation)) {
                        Files.copy(src, desitnation);
                    }
                } catch (IOException e) {
                    throw new StorageException(e);
                }
            });
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public void deleteStorage() {
        deleteHome();
        groups.clear();
        objectNames.clear();
        close();
    }

    private void deleteHome() {
        try (final Stream<Path> stream = walk(home)) {
            stream.sorted(reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (final IOException e) {
                            throw new StorageException(e);
                        }
                    });
        } catch (final IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    public void close() {
        STORAGES.remove(home);
    }

    @Override
    public String toString() {
        final String format = "Storage(home='%s', #groups=%s, #elements_per_group=%s)";

        final String homeAsString = this.home.toString();
        final String numberOfGroups = "" + groups.size();
        final String numberOfEntries = groups.entrySet()
                .stream()
                .map(Pair::of)
                .sorted(comparing(p -> p.second.name()))
                .map(Pair::getFirst)
                .map(objectNames::get)
                .filter(Objects::nonNull)
                .map(Set::size)
                .collect(toList())
                .toString();

        return format(
                format,
                homeAsString,
                numberOfGroups,
                numberOfEntries
        );
    }
}
