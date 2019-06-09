package fr.redrelay.spongecreeperheal.dependency.factory;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.dependency.model.DependencyModel;
import fr.redrelay.spongecreeperheal.dependency.rule.DependencyRule;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;

import java.util.Map;
import java.util.Optional;

/**
 * Create dependency models for Chorus Flower
 */
public class ChorusFlowerDependencyFactory extends ChorusDependencyFactory {

    public ChorusFlowerDependencyFactory(DependencyRule rule) {
        super(rule);
    }

    /**
     * If bottom block is contained into the index or is a chorus plant or a end stone, return it
     * Else it takes the first founded horizontal adjacent and contained into index chorus plant
     * @param blockSnapshot
     * @param index
     * @return
     */
    @Override
    public Optional<DependencyModel<Vector3i>> build(BlockSnapshot blockSnapshot, Map<Vector3i, BlockState> index) {
        final Optional<DependencyModel<Vector3i>> optBlockDown = super.build(blockSnapshot, index);
        if(optBlockDown.isPresent()) {
            return optBlockDown;
        }

        return sideDependencyStream(blockSnapshot, index).findFirst();
    }
}
