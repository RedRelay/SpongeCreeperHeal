package fr.redrelay.spongecreeperheal.engine.dependency;

import fr.redrelay.dependency.model.DependencyModel;
import fr.redrelay.spongecreeperheal.engine.dependency.rule.DependencyRule;

import java.util.Optional;

public interface DependencyFactory<K, T, M> {
     Optional<DependencyModel<T>> build(K identity, M index);
     DependencyRule getSource();
}
