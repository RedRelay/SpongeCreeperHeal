package fr.redrelay.dependency.model;

import java.util.Set;

public interface DependencyModel<T> {
    /**
     * Returns true if the node still has dependency, false else
     * @return
     */
    boolean hasDependency();

    /**
     * Gets the Set of remaining dependency
     * Should be unmodifiable
     * @return
     */
    Set<T> getDependencies();

    /**
     * Remove a dependency
     * @param dependency
     */
    void removeDependency(T dependency);
}
