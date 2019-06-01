package fr.redrelay.dependency.model;

import java.util.Collections;
import java.util.Set;

public class NoDependencyModel implements DependencyModel {

    private final static NoDependencyModel INSTANCE = new NoDependencyModel();

    private NoDependencyModel() {}

    @Override
    public boolean hasDependency() {
        return false;
    }

    @Override
    public Set getDependencies() {
        return Collections.emptySet();
    }

    @Override
    public void removeDependency(Object dependency) { }

    public static NoDependencyModel getInstance() { return INSTANCE; }
}
