package fr.redrelay.dependency.model;

import java.util.Set;

public interface DependencyModel<T> {
    boolean hasDependency();
    Set<T> getDependencies();
    void removeDependency(T dependency);
}
