package fr.redrelay.spongecreeperheal.dependency.provider.impl;

import fr.redrelay.dependency.model.DependencyModel;
import fr.redrelay.spongecreeperheal.accessor.impl.HealableBlockAccessor;
import fr.redrelay.spongecreeperheal.dependency.provider.ChorusDependencyProvider;
import fr.redrelay.spongecreeperheal.dependency.rule.DependencyRule;
import fr.redrelay.spongecreeperheal.healable.atom.block.HealableBlock;
import org.spongepowered.api.block.BlockSnapshot;

import java.util.Optional;

/**
 * Create dependency models for Chorus Flower
 */
public class ChorusFlowerDependencyProvider extends ChorusDependencyProvider {

    public ChorusFlowerDependencyProvider(DependencyRule rule) {
        super(rule);
    }

    /**
     * If bottom block is contained into the index or is a chorus plant or a end stone, return it
     * Else it takes the first founded horizontal adjacent and contained into index chorus plant
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

        return sideDependencyStream(blockSnapshot, accessor).findFirst();
    }
}
