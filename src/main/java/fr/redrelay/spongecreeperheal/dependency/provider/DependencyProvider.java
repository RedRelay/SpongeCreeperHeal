package fr.redrelay.spongecreeperheal.dependency.provider;

import fr.redrelay.dependency.model.DependencyModel;
import fr.redrelay.spongecreeperheal.accessor.impl.HealableBlockAccessor;
import fr.redrelay.spongecreeperheal.dependency.rule.DependencyRule;
import fr.redrelay.spongecreeperheal.healable.atom.block.HealableBlock;
import org.spongepowered.api.block.BlockSnapshot;

import java.util.Optional;

public interface DependencyProvider {
     Optional<DependencyModel<HealableBlock>> provide(BlockSnapshot block, HealableBlockAccessor accessor);
     DependencyRule getSource();
}
