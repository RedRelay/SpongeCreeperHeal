package fr.redrelay.spongecreeperheal.engine.dependency.factory;

import org.spongepowered.api.util.Direction;

public class DownLayedDependencyFactory extends RelativePositionDependencyFactory{
    private static final DownLayedDependencyFactory INSTANCE = new DownLayedDependencyFactory();
    private DownLayedDependencyFactory() {
        super(Direction.DOWN);
    }

    public static DownLayedDependencyFactory getInstance() {
        return INSTANCE;
    }
}
