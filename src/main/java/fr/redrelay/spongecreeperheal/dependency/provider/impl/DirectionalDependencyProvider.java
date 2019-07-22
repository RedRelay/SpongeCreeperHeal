package fr.redrelay.spongecreeperheal.dependency.provider.impl;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.dependency.model.BasicDependencyModel;
import fr.redrelay.dependency.model.DependencyModel;
import fr.redrelay.spongecreeperheal.SpongeCreeperHeal;
import fr.redrelay.spongecreeperheal.dependency.provider.AbstractDependencyProvider;
import fr.redrelay.spongecreeperheal.dependency.rule.DependencyRule;
import fr.redrelay.spongecreeperheal.registry.accessor.BlockStateAccessor;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableDirectionalData;
import org.spongepowered.api.data.manipulator.mutable.block.DirectionalData;
import org.spongepowered.api.util.Direction;

import java.util.Optional;
/**
 * Used to create Dependency models based on block having DirectionalData
 */
public class DirectionalDependencyProvider extends AbstractDependencyProvider {

    public static class NoDirectionalException extends RuntimeException {
        public NoDirectionalException(String message) {
            super(message);
        }
    }

    /**
     * Used to created Dependency models based on hardcoded block having DirectionalData
     */
    public static class Static extends DirectionalDependencyProvider {

        private final Direction direction;

        public Static(DependencyRule rule, Direction direction) {
            super(rule);
            this.direction = direction;
        }

        @Override
        protected Direction getDirection(BlockState block) {
            return direction;
        }
    }

    private final boolean isOpposite;

    /**
     * Create a DirectionalDependencyProvider
     * @param rule
     * @param isOpposite if you must get the opposite direction to have block offset
     */
    public DirectionalDependencyProvider(DependencyRule rule, boolean isOpposite) {
        super(rule);
        this.isOpposite = isOpposite;
    }

    public DirectionalDependencyProvider(DependencyRule rule) {
        this(rule, false);
    }

    /**
     * Looks for the specified direction and retains it only if it is contained in index
     * @param currentBlock
     * @param accessor
     * @return
     */
    @Override
    public Optional<DependencyModel<Vector3i>> provide(BlockSnapshot currentBlock, BlockStateAccessor accessor) {
        try {
            final Vector3i dependency = currentBlock.getPosition().add(getDirection(currentBlock.getState()).asBlockOffset());
            final Optional<BlockState> optDependencyBlockState = accessor.get(dependency);
            if (optDependencyBlockState.isPresent()) {
                return Optional.of(BasicDependencyModel.createUniqueDependency(dependency));
            }
        }catch(NoDirectionalException error) {
            SpongeCreeperHeal.getLogger().warn(error.getMessage());
        }

        return Optional.empty();
    }

    protected Direction getDirection(BlockState block) {
        final Optional<ImmutableDirectionalData> data = block.get(ImmutableDirectionalData.class);
        if(!data.isPresent() || !data.get().direction().exists()) {
            throw new NoDirectionalException(this.getClass().getSimpleName()+" configured block state without any "+ DirectionalData.class.getSimpleName() +" : "+block.getType().getName());
        }

        final Direction direction = data.get().direction().get();
        if(isOpposite) {
            return direction.getOpposite();
        }
        return direction;
    }
}
