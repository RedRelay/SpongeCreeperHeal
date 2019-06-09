package fr.redrelay.dependency.model;

public class AndDependencyModel<T> extends ComplexDependencyModel<T>{

    public AndDependencyModel(DependencyModel<T>... dependencyModels) {
        super(dependencyModels);
    }

    /**
     *
     * @return true if at least one nested dependency still has dependency
     */
    @Override
    public boolean hasDependency() {
        return dependencies.parallelStream().anyMatch(DependencyModel::hasDependency);
    }
}
