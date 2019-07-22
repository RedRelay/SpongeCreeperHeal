package fr.redrelay.spongecreeperheal.accessor;

import java.util.Optional;

public interface Accessor<K,V> {
    Optional<V> get(K key);
}
