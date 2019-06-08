package fr.redrelay.dependency.model;

public class AndDependencyModel<T> extends ComplexDependencyModel<T>{

    public AndDependencyModel(DependencyModel<T>... dependencyModels) {
        super(dependencyModels);
    }

    @Override
    public boolean hasDependency() {
        return dependencies.parallelStream().anyMatch(DependencyModel::hasDependency);
    }
}
