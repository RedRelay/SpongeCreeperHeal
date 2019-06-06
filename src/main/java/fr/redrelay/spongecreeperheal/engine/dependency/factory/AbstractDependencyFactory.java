package fr.redrelay.spongecreeperheal.engine.dependency.factory;

import fr.redrelay.spongecreeperheal.engine.dependency.DependencyFactory;
import fr.redrelay.spongecreeperheal.engine.dependency.rule.DependencyRule;

public abstract class AbstractDependencyFactory implements DependencyFactory{

    final DependencyRule sourceRule;

    public AbstractDependencyFactory(DependencyRule rule) {
        this.sourceRule = rule;
    }

    @Override
    public DependencyRule getSource() {
        return sourceRule;
    }
}
