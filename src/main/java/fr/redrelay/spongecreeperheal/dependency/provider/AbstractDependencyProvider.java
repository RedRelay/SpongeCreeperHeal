package fr.redrelay.spongecreeperheal.dependency.provider;

import fr.redrelay.spongecreeperheal.dependency.rule.DependencyRule;

/**
 * Template used to assign a rule for a DependencyProvider
 */
public abstract class AbstractDependencyProvider implements DependencyProvider {

    private final DependencyRule sourceRule;

    public AbstractDependencyProvider(DependencyRule rule) {
        this.sourceRule = rule;
    }

    @Override
    public DependencyRule getSource() {
        return sourceRule;
    }
}
