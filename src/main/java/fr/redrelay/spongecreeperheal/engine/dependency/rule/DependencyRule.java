package fr.redrelay.spongecreeperheal.engine.dependency.rule;

public interface DependencyRule {
    void registerDependencies();
    default String getName() {
        return this.getClass().getName();
    }
}
