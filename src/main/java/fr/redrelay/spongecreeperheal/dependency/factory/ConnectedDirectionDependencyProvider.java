package fr.redrelay.spongecreeperheal.dependency.factory;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.dependency.model.BasicDependencyModel;
import fr.redrelay.dependency.model.DependencyModel;
import fr.redrelay.spongecreeperheal.dependency.rule.DependencyRule;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableConnectedDirectionData;
import org.spongepowered.api.data.manipulator.mutable.block.ConnectedDirectionData;

import java.util.Map;
import java.util.Optional;

/**
 * Used to create Dependency models based on block having ConnectedDirectionData
 */
public abstract class ConnectedDirectionDependencyProvider extends AbstractDependencyProvider {

    public static class NoConnectedDirectionException extends RuntimeException {
        public NoConnectedDirectionException(String message) {
            super(message);
        }
    }

    public ConnectedDirectionDependencyProvider(DependencyRule rule) {
        super(rule);
    }

    /**
     * Iterate over all direction matchning with ConnectedDirectionData of block explosion
     * Retains only when is contained into index
     * @param blockSnapshot
     * @param index
     * @return
     */
    @Override
    public Optional<DependencyModel<Vector3i>> provide(BlockSnapshot blockSnapshot, Map<Vector3i, BlockState> index) {

        final Optional<ImmutableConnectedDirectionData> data = blockSnapshot.getState().get(ImmutableConnectedDirectionData.class);
        if(!data.isPresent() || !data.get().connectedDirections().exists()) {
            throw new NoConnectedDirectionException(this.getClass().getSimpleName()+" configured block state without any "+ ConnectedDirectionData.class.getSimpleName() +" : "+blockSnapshot.getState().getType().getName());
        }

        final DependencyModel[] dependencies = data.get().connectedDirections().get().parallelStream()
                    .map(direction -> blockSnapshot.getPosition().add(direction.asBlockOffset()))
                    .filter(index::containsKey)
                    .map(BasicDependencyModel::createUniqueDependency)
                    .toArray(DependencyModel[]::new);


        if(dependencies.length == 0) {
            return Optional.empty();
        }

        if(dependencies.length == 1 ) {
            return Optional.of(dependencies[0]);
        }

        return Optional.of(merge(dependencies));
    }

    /**
     * If multiple direction was found, how to merge them ? Or ? And ?
     * @param dependencies
     * @return
     */
    protected abstract DependencyModel<Vector3i> merge(DependencyModel<Vector3i>... dependencies);
}
