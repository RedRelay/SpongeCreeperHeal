package fr.redrelay.dependency.model;

public class OrDependencyModel<T> extends ComplexDependencyModel<T>{

    public OrDependencyModel(DependencyModel<T>... dependencyModels) {
        super(dependencyModels);
    }

    @Override
    public boolean hasDependency() {
        return dependencies.parallelStream().allMatch(DependencyModel::hasDependency);
    }
}
