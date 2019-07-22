package fr.redrelay.spongecreeperheal.dependency.provider.impl;

import fr.redrelay.dependency.model.BasicDependencyModel;
import fr.redrelay.dependency.model.DependencyModel;
import fr.redrelay.dependency.model.OrDependencyModel;
import fr.redrelay.spongecreeperheal.accessor.impl.HealableBlockAccessor;
import fr.redrelay.spongecreeperheal.dependency.provider.AbstractDependencyProvider;
import fr.redrelay.spongecreeperheal.dependency.rule.DependencyRule;
import fr.redrelay.spongecreeperheal.healable.atom.block.HealableBlock;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.property.block.FlammableProperty;
import org.spongepowered.api.util.Direction;

import java.util.Arrays;
import java.util.Optional;

/**
 * Create fire dependency models
 */
public class FireDependencyProvider extends AbstractDependencyProvider {

    public FireDependencyProvider(DependencyRule rule) {
        super(rule);
    }

    /**
     * Looks for all flammable block around and retains only those contained in index
     * @param blockSnapshot
     * @param accessor
     * @return
     */
    @Override
    public Optional<DependencyModel<HealableBlock>> provide(BlockSnapshot blockSnapshot, HealableBlockAccessor accessor) {

        final DependencyModel[] dependencies = Arrays.asList(Direction.values()).parallelStream()
                .filter(Direction::isCardinal)
                .map(direction -> blockSnapshot.getPosition().add(direction.asBlockOffset()))
                .filter(pos -> {
                    final Optional<HealableBlock> optSideBlock = accessor.get(pos);
                    if(!optSideBlock.isPresent()) return false;
                    final Optional<FlammableProperty> optFlammable = optSideBlock.get().getBlockSnapshots().get(pos).getState().getType().getProperty(FlammableProperty.class);
                    return optFlammable.isPresent() && optFlammable.get().getValue();
                })
                .map(BasicDependencyModel::createUniqueDependency)
                .toArray(DependencyModel[]::new);

        if(dependencies.length == 0) {
            return Optional.empty();
        }

        if(dependencies.length == 1) {
            return Optional.of(dependencies[0]);
        }

        return Optional.of(new OrDependencyModel<>(dependencies));
    }
}
