package fr.redrelay.dependency.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class BasicDependencyModel<T> implements DependencyModel<T> {

    private final Set<T> dependencies;

    public BasicDependencyModel(Set<T> dependencies) {
        this.dependencies = new HashSet<>(dependencies);
    }

    private BasicDependencyModel(T dependency) {
        this.dependencies = new HashSet<>(Arrays.asList(dependency));
    }

    @Override
    public boolean hasDependency() {
        return !dependencies.isEmpty();
    }

    /**
     *
     * @return An unmodifiable Set of dependencies
     */
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
