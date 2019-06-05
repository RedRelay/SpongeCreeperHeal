package fr.redrelay.spongecreeperheal.engine.dependency.factory;

import fr.redrelay.spongecreeperheal.engine.dependency.rule.DependencyRule;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableDirectionalData;
import org.spongepowered.api.util.Direction;

import java.util.Optional;

public class FacingDependencyFactory extends AbstractRelativePositionDependencyFactory {

    private final boolean isOpposite;

    public FacingDependencyFactory(DependencyRule rule, boolean isOpposite) {
        super(rule);
        this.isOpposite = isOpposite;
    }

    public FacingDependencyFactory(DependencyRule rule) {
        this(rule, false);
    }

    @Override
    protected Direction getDirection(BlockState block) {
        Optional<ImmutableDirectionalData> data = block.get(ImmutableDirectionalData.class);
        if(!data.isPresent() || !data.get().direction().exists()) {
            throw new AbstractRelativePositionDependencyFactory.NoDirectionException("FacingDependencyFactory configured block state without any DirectionalData : "+block.getType().getName());
        }

        final Direction direction = data.get().direction().get();
        if(isOpposite) {
            return direction.getOpposite();
        }
        return direction;
    }
}
