package fr.redrelay.spongecreeperheal.engine.dependency.factory;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.dependency.model.BasicDependencyModel;
import fr.redrelay.dependency.model.DependencyModel;
import fr.redrelay.spongecreeperheal.engine.dependency.rule.DependencyRule;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableConnectedDirectionData;

import java.util.Map;
import java.util.Optional;

public abstract class ConnectedDirectionDependencyFactory extends AbstractDependencyFactory {

    public static class NoConnectedDataException extends RuntimeException {
        public NoConnectedDataException(String message) {
            super(message);
        }
    }

    public ConnectedDirectionDependencyFactory(DependencyRule rule) {
        super(rule);
    }

    @Override
    public Optional<DependencyModel<Vector3i>> build(BlockSnapshot blockSnapshot, Map<Vector3i, BlockState> index) {

        final Optional<ImmutableConnectedDirectionData> data = blockSnapshot.getState().get(ImmutableConnectedDirectionData.class);
        if(!data.isPresent() || !data.get().connectedDirections().exists()) {
            throw new NoConnectedDataException(this.getClass().getSimpleName()+"configured block state without any DirectionalData : "+blockSnapshot.getState().getType().getName());
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

    protected abstract DependencyModel<Vector3i> merge(DependencyModel<Vector3i>... dependencies);
}
