package fr.redrelay.spongecreeperheal.engine.dependency.factory;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.dependency.model.BasicDependencyModel;
import fr.redrelay.dependency.model.DependencyModel;
import fr.redrelay.spongecreeperheal.engine.dependency.rule.DependencyRule;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.util.Direction;

import java.util.Map;
import java.util.Optional;

public class RelativePositionDependencyFactory extends AbstractDependencyFactory {

    private final Direction direction;

    public RelativePositionDependencyFactory(DependencyRule rule, Direction direction) {
        super(rule);
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
