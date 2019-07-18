package fr.redrelay.spongecreeperheal.dependency.factory;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.dependency.model.BasicDependencyModel;
import fr.redrelay.dependency.model.DependencyModel;
import fr.redrelay.spongecreeperheal.adapter.DirectionAdapter;
import fr.redrelay.spongecreeperheal.dependency.rule.DependencyRule;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.util.Direction;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Regroup Chorus common dependency
 */
public abstract class ChorusDependencyProvider extends AbstractDependencyProvider {

    protected ChorusDependencyProvider(DependencyRule rule) {
        super(rule);
    }

    /**
     * Check if the block below if a Chorus Plant or a End Stone
     * @param blockSnapshot
     * @param index
     * @return
     */
    @Override
    public Optional<DependencyModel<Vector3i>> provide(BlockSnapshot blockSnapshot, Map<Vector3i, BlockState> index) {
        final Vector3i posDown = blockSnapshot.getPosition().add(Direction.DOWN.asBlockOffset());
        final BlockState blockDown = index.get(posDown);
        if(blockDown != null && (blockDown.getType().equals(BlockTypes.CHORUS_PLANT) || blockDown.getType().equals(BlockTypes.END_STONE))) {
            return Optional.of(BasicDependencyModel.createUniqueDependency(posDown));
        }
        return Optional.empty();
    }

    /**
     * Create a stream of all chorus plant horizontally adjacent to the block explosion and contained into the index
     * @param blockSnapshot
     * @param index
     * @return
     */
    protected Stream<DependencyModel<Vector3i>> sideDependencyStream(BlockSnapshot blockSnapshot, Map<Vector3i, BlockState> index) {
        return  Arrays.asList(DirectionAdapter.HORIZONTAL).stream()
                .map(direction -> blockSnapshot.getPosition().add(direction.asBlockOffset()))
                .filter(pos -> index.containsKey(pos)).filter(pos -> index.get(pos).getType().equals(BlockTypes.CHORUS_PLANT))
                .map(BasicDependencyModel::createUniqueDependency);
    }


}
