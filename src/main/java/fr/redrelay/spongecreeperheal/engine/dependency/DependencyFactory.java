package fr.redrelay.spongecreeperheal.engine.dependency;

import fr.redrelay.dependency.model.DependencyModel;

import java.util.Optional;

public interface DependencyFactory<K, T, M> {
     Optional<DependencyModel<T>> build(K identity, M index);
}
