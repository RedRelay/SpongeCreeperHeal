package fr.redrelay.dependency.model;

public class OrDependencyModel<T> extends ComplexDependencyModel<T>{

    public OrDependencyModel(DependencyModel<T>... dependencyModels) {
        super(dependencyModels);
    }

    /**
     *
     * @return true if all nested dependencies still have dependencies, else false
     */
    @Override
    public boolean hasDependency() {
        return dependencies.parallelStream().allMatch(DependencyModel::hasDependency);
    }
}
