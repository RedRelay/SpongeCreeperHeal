package fr.redrelay.spongecreeperheal.dependency.factory.impl;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.dependency.model.BasicDependencyModel;
import fr.redrelay.dependency.model.DependencyModel;
import fr.redrelay.dependency.model.OrDependencyModel;
import fr.redrelay.spongecreeperheal.dependency.factory.AbstractDependencyFactory;
import fr.redrelay.spongecreeperheal.dependency.rule.DependencyRule;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.property.block.FlammableProperty;
import org.spongepowered.api.util.Direction;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

/**
 * Create fire dependency models
 */
public class FireDependencyFactory extends AbstractDependencyFactory {

    public FireDependencyFactory(DependencyRule rule) {
        super(rule);
    }

    /**
     * Looks for all flammable block around and retains only those contained in index
     * @param blockSnapshot
     * @param index
     * @return
     */
    @Override
    public Optional<DependencyModel<Vector3i>> build(BlockSnapshot blockSnapshot, Map<Vector3i, BlockState> index) {

        final DependencyModel[] dependencies = Arrays.asList(Direction.values()).parallelStream()
                .filter(Direction::isCardinal)
                .filter(pos -> {
                    final BlockState sideBlock = index.get(pos);
                    if(sideBlock == null) return false;
                    final Optional<FlammableProperty> optFlammable = sideBlock.getType().getProperty(FlammableProperty.class);
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
