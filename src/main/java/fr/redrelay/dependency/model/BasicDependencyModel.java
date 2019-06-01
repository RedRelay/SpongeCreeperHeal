package fr.redrelay.dependency.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class BasicDependencyModel<T> implements DependencyModel<T> {

    private final Set<T> dependencies;

    public BasicDependencyModel(Set<T> dependencies) {
        this.dependencies = new HashSet<>(dependencies);
    }

    private BasicDependencyModel(T dependency) {
        this.dependencies = new TreeSet<>();
        this.dependencies.add(dependency);
    }

    @Override
    public boolean hasDependency() {
        return !dependencies.isEmpty();
    }

    @Override
    public Set<T> getDependencies() {
        return Collections.unmodifiableSet(dependencies);
    }

    @Override
    public void removeDependency(T dependency) { dependencies.remove(dependency);
    }

    public static <T> BasicDependencyModel<T> createUniqueDependency(T dependency) {
        return new BasicDependencyModel<>(dependency);
    }
}
