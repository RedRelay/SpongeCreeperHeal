package fr.redrelay.spongecreeperheal.healable.factory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Registry<K,V> {

    private final Map<K, V> registry = new HashMap<>();

    public void register(K key, V value) {
        this.registry.put(key, value);
    }

    public void unregister(K key) {
        this.registry.remove(key);
    }

    public Map<K, V> getRegistry() {
        return Collections.unmodifiableMap(this.registry);
    }

    public Optional<V> getRegistred(K key) {
        return Optional.ofNullable(registry.get(key));
    }
}
