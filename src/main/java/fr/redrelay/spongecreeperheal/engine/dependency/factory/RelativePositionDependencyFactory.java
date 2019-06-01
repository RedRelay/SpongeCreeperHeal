package fr.redrelay.spongecreeperheal.engine.dependency.factory;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.dependency.model.BasicDependencyModel;
import fr.redrelay.dependency.model.DependencyModel;
import fr.redrelay.spongecreeperheal.engine.dependency.DependencyFactory;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.util.Direction;

import java.util.Map;
import java.util.Optional;

public class RelativePositionDependencyFactory implements DependencyFactory<BlockSnapshot, Vector3i, Map<Vector3i, BlockState>> {

    private final Direction direction;

    public RelativePositionDependencyFactory(Direction direction) {
        this.direction = direction;
    }

    @Override
    public Optional<DependencyModel<Vector3i>> build(BlockSnapshot currentBlock, Map<Vector3i, BlockState> index) {
        final Vector3i dependency = currentBlock.getPosition().add(direction.asBlockOffset());
        if (index.containsKey(dependency)) {
            return Optional.of(BasicDependencyModel.createUniqueDependency(dependency));
        }

        return Optional.empty();
    }
}
