package fr.redrelay.spongecreeperheal.registry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Registry<K,V> {

    protected final Map<K, V> registry = new HashMap<>();

    public void register(K key, V value) {
        this.registry.put(key, value);
    }

    public Map<K, V> getRegistry() {
        return Collections.unmodifiableMap(this.registry);
    }

    public Optional<V> get(K key) {
        return Optional.ofNullable(registry.get(key));
    }
}
