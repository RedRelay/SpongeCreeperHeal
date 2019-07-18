package fr.redrelay.spongecreeperheal.dependency.factory.impl;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.dependency.model.DependencyModel;
import fr.redrelay.dependency.model.OrDependencyModel;
import fr.redrelay.spongecreeperheal.dependency.factory.ChorusDependencyProvider;
import fr.redrelay.spongecreeperheal.dependency.rule.DependencyRule;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;

import java.util.Map;
import java.util.Optional;

/**
 * Create dependency models for Chorus Plant
 */
public class ChorusPlantDependencyProvider extends ChorusDependencyProvider {

    public ChorusPlantDependencyProvider(DependencyRule rule) {
        super(rule);
    }

    /**
     * If bottom block is contained into the index or is a chorus plant or a end stone, return it
     * Else dependency is one of the contained into index horizontal adjacent chorus plant
     * @param blockSnapshot
     * @param index
     * @return
     */
    @Override
    public Optional<DependencyModel<Vector3i>> provide(BlockSnapshot blockSnapshot, Map<Vector3i, BlockState> index) {
        final Optional<DependencyModel<Vector3i>> optBlockDown = super.provide(blockSnapshot, index);
        if(optBlockDown.isPresent()) {
            return optBlockDown;
        }

        final DependencyModel[] sideDependencies = sideDependencyStream(blockSnapshot, index)
                .toArray(DependencyModel[]::new);

        if(sideDependencies.length == 0) {
            return Optional.empty();
        }

        if(sideDependencies.length == 1) {
            return Optional.of(sideDependencies[0]);
        }

        return Optional.of(new OrDependencyModel<Vector3i>(sideDependencies));
    }
}
