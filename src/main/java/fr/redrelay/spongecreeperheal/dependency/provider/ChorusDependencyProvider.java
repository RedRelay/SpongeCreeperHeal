package fr.redrelay.spongecreeperheal.dependency.provider;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.dependency.model.BasicDependencyModel;
import fr.redrelay.dependency.model.DependencyModel;
import fr.redrelay.spongecreeperheal.accessor.impl.HealableBlockAccessor;
import fr.redrelay.spongecreeperheal.adapter.DirectionAdapter;
import fr.redrelay.spongecreeperheal.dependency.rule.DependencyRule;
import fr.redrelay.spongecreeperheal.healable.atom.block.HealableBlock;
import org.spongepowered.api.block.BlockSnapshot;
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
    public Optional<DependencyModel<HealableBlock>> provide(BlockSnapshot blockSnapshot, HealableBlockAccessor accessor) {
        final Vector3i posDown = blockSnapshot.getPosition().add(Direction.DOWN.asBlockOffset());
        final Optional<HealableBlock> optBlockDown = accessor.get(posDown);
        if(optBlockDown.isPresent()) {
            final BlockType blockType = optBlockDown.get().getBlockSnapshots().get(posDown).getState().getType();
            if(blockType.equals(BlockTypes.CHORUS_PLANT) || blockType.equals(BlockTypes.END_STONE)) {
                return Optional.of(BasicDependencyModel.createUniqueDependency(optBlockDown.get()));
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
    protected Stream<DependencyModel<HealableBlock>> sideDependencyStream(BlockSnapshot blockSnapshot, HealableBlockAccessor accessor) {
        return  Arrays.stream(DirectionAdapter.HORIZONTAL)
                .map(direction -> blockSnapshot.getPosition().add(direction.asBlockOffset()))
                .filter(pos -> {
                    final Optional<HealableBlock> optHealableBlock = accessor.get(pos);
                    return optHealableBlock.isPresent() && optHealableBlock.get().getBlockSnapshots().get(pos).getState().getType().equals(BlockTypes.CHORUS_PLANT);
                })
                .map(pos -> accessor.get(pos).get())
                .map(BasicDependencyModel::createUniqueDependency);
    }


}
