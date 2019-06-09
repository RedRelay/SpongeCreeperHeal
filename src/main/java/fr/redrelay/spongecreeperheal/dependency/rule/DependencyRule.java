package fr.redrelay.spongecreeperheal.dependency.rule;

public interface DependencyRule {
    void registerDependencies();
    default String getName() {
        return this.getClass().getName();
    }
}
