package fr.redrelay.spongecreeperheal.dependency.provider.impl;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.dependency.model.BasicDependencyModel;
import fr.redrelay.dependency.model.DependencyModel;
import fr.redrelay.dependency.model.OrDependencyModel;
import fr.redrelay.spongecreeperheal.dependency.provider.AbstractDependencyProvider;
import fr.redrelay.spongecreeperheal.dependency.rule.DependencyRule;
import fr.redrelay.spongecreeperheal.healable.factory.BlockStateAccessor;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
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
    public Optional<DependencyModel<Vector3i>> provide(BlockSnapshot blockSnapshot, BlockStateAccessor accessor) {

        final DependencyModel[] dependencies = Arrays.asList(Direction.values()).parallelStream()
                .filter(Direction::isCardinal)
                .filter(direction -> {
                    final Optional<BlockState> optSideBlock = accessor.get(blockSnapshot.getPosition().add(direction.asBlockOffset()));
                    if(!optSideBlock.isPresent()) return false;
                    final Optional<FlammableProperty> optFlammable = optSideBlock.get().getType().getProperty(FlammableProperty.class);
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
