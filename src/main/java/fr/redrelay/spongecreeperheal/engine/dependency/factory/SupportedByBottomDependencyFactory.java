package fr.redrelay.spongecreeperheal.engine.dependency.factory;

import org.spongepowered.api.util.Direction;

public class SupportedByBottomDependencyFactory extends RelativePositionDependencyFactory{
    public SupportedByBottomDependencyFactory() {
        super(Direction.DOWN);
    }
}
