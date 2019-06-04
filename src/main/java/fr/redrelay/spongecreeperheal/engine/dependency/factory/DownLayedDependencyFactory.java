package fr.redrelay.spongecreeperheal.engine.dependency.factory;

import fr.redrelay.spongecreeperheal.engine.dependency.rule.DependencyRule;
import org.spongepowered.api.util.Direction;

public class DownLayedDependencyFactory extends RelativePositionDependencyFactory{

    public DownLayedDependencyFactory(DependencyRule rule) {
        super(rule, Direction.DOWN);
    }
}
