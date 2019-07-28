package fr.redrelay.spongecreeperheal.dependency.provider;

import fr.redrelay.dependency.model.BasicDependencyModel;
import fr.redrelay.dependency.model.DependencyModel;
import fr.redrelay.spongecreeperheal.accessor.impl.HealableBlockAccessor;
import fr.redrelay.spongecreeperheal.dependency.rule.DependencyRule;
import fr.redrelay.spongecreeperheal.healable.atom.block.HealableBlock;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableConnectedDirectionData;
import org.spongepowered.api.data.manipulator.mutable.block.ConnectedDirectionData;

import java.util.Optional;

/**
 * Used to create Dependency models based on block having ConnectedDirectionData
 */
public abstract class ConnectedDirectionDependencyProvider extends AbstractDependencyProvider {

    public static class NoConnectedDirectionException extends RuntimeException {
        public NoConnectedDirectionException(String message) {
            super(message);
        }
    }

    public ConnectedDirectionDependencyProvider(DependencyRule rule) {
        super(rule);
    }

    /**
     * Iterate over all direction matchning with ConnectedDirectionData of block explosion
     * Retains only when is contained into index
     * @param blockSnapshot
     * @param accessor
     * @return
     */
    @Override
    public Optional<DependencyModel<HealableBlock>> provide(BlockSnapshot blockSnapshot, HealableBlockAccessor accessor) {

        final Optional<ImmutableConnectedDirectionData> data = blockSnapshot.getState().get(ImmutableConnectedDirectionData.class);
        if(!data.isPresent() || !data.get().connectedDirections().exists()) {
            throw new NoConnectedDirectionException(this.getClass().getSimpleName()+" configured block state without any "+ ConnectedDirectionData.class.getSimpleName() +" : "+blockSnapshot.getState().getType().getName());
        }

        final DependencyModel[] dependencies = data.get().connectedDirections().get().parallelStream()
                    .map(direction -> blockSnapshot.getPosition().add(direction.asBlockOffset()))
                    .map(accessor::get)
                    .filter(Optional::isPresent)
                    .map(BasicDependencyModel::createUniqueDependency)
                    .toArray(DependencyModel[]::new);


        if(dependencies.length == 0) {
            return Optional.empty();
        }

        if(dependencies.length == 1 ) {
            return Optional.of(dependencies[0]);
        }

        return Optional.of(merge(dependencies));
    }

    /**
     * If multiple direction was found, how to merge them ? Or ? And ?
     * @param dependencies
     * @return
     */
    protected abstract DependencyModel<HealableBlock> merge(DependencyModel<HealableBlock>... dependencies);
}
