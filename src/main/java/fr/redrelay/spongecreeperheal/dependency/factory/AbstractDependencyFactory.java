package fr.redrelay.spongecreeperheal.dependency.factory;

import fr.redrelay.spongecreeperheal.dependency.DependencyFactory;
import fr.redrelay.spongecreeperheal.dependency.rule.DependencyRule;

/**
 * Template used to assign a rule for a DependencyFactory
 */
public abstract class AbstractDependencyFactory implements DependencyFactory {

    private final DependencyRule sourceRule;

    public AbstractDependencyFactory(DependencyRule rule) {
        this.sourceRule = rule;
    }

    @Override
    public DependencyRule getSource() {
        return sourceRule;
    }
}
