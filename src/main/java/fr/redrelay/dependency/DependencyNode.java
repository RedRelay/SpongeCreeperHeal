package fr.redrelay.dependency;

import fr.redrelay.dependency.model.DependencyModel;


public class DependencyNode<T> {

    private final T entry;
    private final DependencyModel<T> dependencyModel;


    public DependencyNode(T entry, DependencyModel<T> dependencyModel) {
        this.entry = entry;
        this.dependencyModel = dependencyModel;
    }

    public T getEntry() {
        return entry;
    }

    public DependencyModel<T> getDependencyModel() {
        return dependencyModel;
    }
}
