package fr.redrelay.spongecreeperheal.engine.dependency.factory;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.dependency.model.BasicDependencyModel;
import fr.redrelay.dependency.model.DependencyModel;
import fr.redrelay.spongecreeperheal.SpongeCreeperHeal;
import fr.redrelay.spongecreeperheal.engine.dependency.rule.DependencyRule;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.util.Direction;

import java.util.Map;
import java.util.Optional;

public abstract class AbstractRelativePositionDependencyFactory extends AbstractDependencyFactory {

    public static class NoDirectionException extends RuntimeException {
        public NoDirectionException(String message) {
            super(message);
        }
    }

    public AbstractRelativePositionDependencyFactory(DependencyRule rule) {
        super(rule);
    }

    @Override
    public Optional<DependencyModel<Vector3i>> build(BlockSnapshot currentBlock, Map<Vector3i, BlockState> index) {
        try {
            final Vector3i dependency = currentBlock.getPosition().add(getDirection(currentBlock.getState()).asBlockOffset());
            if (index.containsKey(dependency)) {
                return Optional.of(BasicDependencyModel.createUniqueDependency(dependency));
            }
        }catch(NoDirectionException error) {
            SpongeCreeperHeal.getLogger().warn(error.getMessage());
        }

        return Optional.empty();
    }

    protected abstract Direction getDirection(BlockState block);

}
