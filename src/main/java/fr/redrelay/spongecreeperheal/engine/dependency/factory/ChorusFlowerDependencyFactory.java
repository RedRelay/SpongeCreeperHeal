package fr.redrelay.spongecreeperheal.engine.dependency.factory;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.dependency.model.DependencyModel;
import fr.redrelay.spongecreeperheal.engine.dependency.rule.DependencyRule;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;

import java.util.Map;
import java.util.Optional;

public class ChorusFlowerDependencyFactory extends ChorusDependencyFactory {

    public ChorusFlowerDependencyFactory(DependencyRule rule) {
        super(rule);
    }

    @Override
    public Optional<DependencyModel<Vector3i>> build(BlockSnapshot blockSnapshot, Map<Vector3i, BlockState> index) {
        final Optional<DependencyModel<Vector3i>> optBlockDown = super.build(blockSnapshot, index);
        if(optBlockDown.isPresent()) {
            return optBlockDown;
        }

        return sideDependencyStream(blockSnapshot, index).findFirst();
    }
}
