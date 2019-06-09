package fr.redrelay.dependency.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class ComplexDependencyModel<T> implements DependencyModel<T>{

    public static class NotEnoughtOperandException extends RuntimeException {
        public NotEnoughtOperandException(DependencyModel[] operands) {
            super("Unable to create "+ComplexDependencyModel.class.getSimpleName()+" without at least 2 operands, got "+operands.length);
        }
    }

    protected final Set<DependencyModel<T>> dependencies;


    /**
     * Creates a new ComplexDependencyModel
     * @param dependencyModels a list of dependency model to aggregate, minimum size is 2
     */
    public ComplexDependencyModel(DependencyModel<T>... dependencyModels) {
        if(dependencyModels.length < 2) {
            throw new NotEnoughtOperandException(dependencyModels);
        }
        this.dependencies = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(dependencyModels)));
    }

    /**
     *
     * @return an unmodifiable set of dependencies
     */
    @Override
    public Set<T> getDependencies() {
        final Set<T> set = new HashSet<>(dependencies.parallelStream().reduce(0, (acc, dependency) -> acc + dependency.getDependencies().size(), Integer::sum));
        dependencies.forEach(dependency -> set.addAll(dependency.getDependencies()));
        return Collections.unmodifiableSet(set);
    }

    @Override
    public void removeDependency(T dependency) {
        dependencies.parallelStream().forEach(dep -> dep.removeDependency(dependency));
    }
}
