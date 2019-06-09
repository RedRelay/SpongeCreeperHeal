package fr.redrelay.spongecreeperheal.engine.dependency.factory;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.dependency.model.BasicDependencyModel;
import fr.redrelay.dependency.model.DependencyModel;
import fr.redrelay.spongecreeperheal.engine.dependency.factory.helper.DependencyFactoryHelper;
import fr.redrelay.spongecreeperheal.engine.dependency.rule.DependencyRule;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.util.Direction;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class ChorusDependencyFactory extends AbstractDependencyFactory {

    protected ChorusDependencyFactory(DependencyRule rule) {
        super(rule);
    }

    @Override
    public Optional<DependencyModel<Vector3i>> build(BlockSnapshot blockSnapshot, Map<Vector3i, BlockState> index) {
        final Vector3i posDown = blockSnapshot.getPosition().add(Direction.DOWN.asBlockOffset());
        final BlockState blockDown = index.get(posDown);
        if(blockDown != null && (blockDown.getType().equals(BlockTypes.CHORUS_PLANT) || blockDown.getType().equals(BlockTypes.END_STONE))) {
            return Optional.of(BasicDependencyModel.createUniqueDependency(posDown));
        }
        return Optional.empty();
    }

    protected Stream<DependencyModel<Vector3i>> sideDependencyStream(BlockSnapshot blockSnapshot, Map<Vector3i, BlockState> index) {
        return DependencyFactoryHelper.sideBlocks(blockSnapshot, index)
                .filter(pos -> index.get(pos).getType().equals(BlockTypes.CHORUS_PLANT))
                .map(BasicDependencyModel::createUniqueDependency);
    }


}
