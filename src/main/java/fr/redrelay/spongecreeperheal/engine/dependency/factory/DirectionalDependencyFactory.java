package fr.redrelay.spongecreeperheal.engine.dependency.factory;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.dependency.model.BasicDependencyModel;
import fr.redrelay.dependency.model.DependencyModel;
import fr.redrelay.spongecreeperheal.SpongeCreeperHeal;
import fr.redrelay.spongecreeperheal.engine.dependency.rule.DependencyRule;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableDirectionalData;
import org.spongepowered.api.util.Direction;

import java.util.Map;
import java.util.Optional;

public class DirectionalDependencyFactory extends AbstractDependencyFactory {

    public static class NoDirectionalException extends RuntimeException {
        public NoDirectionalException(String message) {
            super(message);
        }
    }

    public static class Static extends DirectionalDependencyFactory {

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

    public DirectionalDependencyFactory(DependencyRule rule, boolean isOpposite) {
        super(rule);
        this.isOpposite = isOpposite;
    }

    public DirectionalDependencyFactory(DependencyRule rule) {
        this(rule, false);
    }

    @Override
    public Optional<DependencyModel<Vector3i>> build(BlockSnapshot currentBlock, Map<Vector3i, BlockState> index) {
        try {
            final Vector3i dependency = currentBlock.getPosition().add(getDirection(currentBlock.getState()).asBlockOffset());
            if (index.containsKey(dependency)) {
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
            throw new NoDirectionalException(this.getClass().getSimpleName()+" configured block state without any DirectionalData : "+block.getType().getName());
        }

        final Direction direction = data.get().direction().get();
        if(isOpposite) {
            return direction.getOpposite();
        }
        return direction;
    }
}
