package fr.redrelay.spongecreeperheal.dependency.provider;

import fr.redrelay.dependency.model.DependencyModel;
import fr.redrelay.spongecreeperheal.accessor.impl.HealableBlockAccessor;
import fr.redrelay.spongecreeperheal.dependency.rule.DependencyRule;
import fr.redrelay.spongecreeperheal.factory.BlockProvider;
import fr.redrelay.spongecreeperheal.healable.atom.block.HealableBlock;

import java.util.Optional;

public interface DependencyProvider extends BlockProvider<HealableBlockAccessor, Optional<DependencyModel<HealableBlock>>> {
     DependencyRule getSource();
}
