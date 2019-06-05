package fr.redrelay.spongecreeperheal.engine.dependency.factory;

import fr.redrelay.spongecreeperheal.engine.dependency.rule.DependencyRule;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.util.Direction;

public class DirectionDependencyFactory extends AbstractRelativePositionDependencyFactory {

    private final Direction direction;

    public DirectionDependencyFactory(DependencyRule rule, Direction direction) {
        super(rule);
        this.direction = direction;
    }

    @Override
    protected Direction getDirection(BlockState block) {
        return direction;
    }
}
