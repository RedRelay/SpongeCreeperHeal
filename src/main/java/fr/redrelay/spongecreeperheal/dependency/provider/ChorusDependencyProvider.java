package fr.redrelay.spongecreeperheal.dependency.provider;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.dependency.model.BasicDependencyModel;
import fr.redrelay.dependency.model.DependencyModel;
import fr.redrelay.spongecreeperheal.adapter.DirectionAdapter;
import fr.redrelay.spongecreeperheal.dependency.rule.DependencyRule;
import fr.redrelay.spongecreeperheal.healable.factory.BlockStateAccessor;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.util.Direction;

import java.util.Arrays;
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
     * @param accessor
     * @return
     */
    @Override
    public Optional<DependencyModel<Vector3i>> provide(BlockSnapshot blockSnapshot, BlockStateAccessor accessor) {
        final Vector3i posDown = blockSnapshot.getPosition().add(Direction.DOWN.asBlockOffset());
        final Optional<BlockState> optBlockDown = accessor.get(posDown);
        if(optBlockDown.isPresent()) {
            final BlockType blockType = optBlockDown.get().getType();
            if(blockType.equals(BlockTypes.CHORUS_PLANT) || blockType.equals(BlockTypes.END_STONE)) {
                return Optional.of(BasicDependencyModel.createUniqueDependency(posDown));
            }
        }
        return Optional.empty();
    }

    /**
     * Create a stream of all chorus plant horizontally adjacent to the block explosion and contained into the index
     * @param blockSnapshot
     * @param accessor
     * @return
     */
    protected Stream<DependencyModel<Vector3i>> sideDependencyStream(BlockSnapshot blockSnapshot, BlockStateAccessor accessor) {
        return  Arrays.asList(DirectionAdapter.HORIZONTAL).stream()
                .map(direction -> blockSnapshot.getPosition().add(direction.asBlockOffset()))
                .filter(pos -> {
                    final Optional<BlockState> optBlockState = accessor.get(pos);
                    return optBlockState.isPresent() && optBlockState.get().getType().equals(BlockTypes.CHORUS_PLANT);
                })
                .map(BasicDependencyModel::createUniqueDependency);
    }


}
