package fr.redrelay.spongecreeperheal.dependency.provider.impl;

import fr.redrelay.dependency.model.DependencyModel;
import fr.redrelay.dependency.model.OrDependencyModel;
import fr.redrelay.spongecreeperheal.accessor.impl.HealableBlockAccessor;
import fr.redrelay.spongecreeperheal.dependency.provider.ChorusDependencyProvider;
import fr.redrelay.spongecreeperheal.dependency.rule.DependencyRule;
import fr.redrelay.spongecreeperheal.healable.atom.block.HealableBlock;
import org.spongepowered.api.block.BlockSnapshot;

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
     * @param accessor
     * @return
     */
    @Override
    public Optional<DependencyModel<HealableBlock>> provide(BlockSnapshot blockSnapshot, HealableBlockAccessor accessor) {
        final Optional<DependencyModel<HealableBlock>> optBlockDown = super.provide(blockSnapshot, accessor);
        if(optBlockDown.isPresent()) {
            return optBlockDown;
        }

        final DependencyModel[] sideDependencies = sideDependencyStream(blockSnapshot, accessor)
                .toArray(DependencyModel[]::new);

        if(sideDependencies.length == 0) {
            return Optional.empty();
        }

        if(sideDependencies.length == 1) {
            return Optional.of(sideDependencies[0]);
        }

        return Optional.of(new OrDependencyModel<HealableBlock>(sideDependencies));
    }
}
