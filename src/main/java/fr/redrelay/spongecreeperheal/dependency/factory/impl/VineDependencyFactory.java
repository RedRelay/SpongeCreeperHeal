package fr.redrelay.spongecreeperheal.dependency.factory.impl;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.dependency.model.BasicDependencyModel;
import fr.redrelay.dependency.model.DependencyModel;
import fr.redrelay.dependency.model.OrDependencyModel;
import fr.redrelay.spongecreeperheal.adapter.DirectionAdapter;
import fr.redrelay.spongecreeperheal.dependency.factory.AbstractDependencyFactory;
import fr.redrelay.spongecreeperheal.dependency.factory.ConnectedDirectionDependencyFactory;
import fr.redrelay.spongecreeperheal.dependency.rule.DependencyRule;
import net.minecraft.block.Block;
import net.minecraft.block.BlockVine;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableConnectedDirectionData;
import org.spongepowered.api.data.manipulator.mutable.block.ConnectedDirectionData;
import org.spongepowered.api.util.Direction;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Used to create vine dependency models
 */
public class VineDependencyFactory extends AbstractDependencyFactory {

    /**
     * Only used to easyly access to isExceptBlockForAttaching from BlockVine which is protected
     */
    private final static class VineAdapter extends BlockVine {

        private VineAdapter() {}

        protected static boolean isExceptBlockForAttaching(BlockType block) {
            return BlockVine.isExceptBlockForAttaching((Block)block);
        }
    }

    public VineDependencyFactory(DependencyRule rule) {
        super(rule);
    }

    /**
     * As native solution seems to be broken @see https://forums.spongepowered.org/t/blockvine-connecteddirectiondata-seems-to-be-broken/29212
     * A custom solution has been developped
     * Create a Or dependency model to wait upside vine block or valid horizontal side block
     * @param blockSnapshot
     * @param index
     * @return
     */
    @Override
    public Optional<DependencyModel<Vector3i>> build(BlockSnapshot blockSnapshot, Map<Vector3i, BlockState> index) {
        final Vector3i posUp = blockSnapshot.getPosition().add(Direction.UP.asBlockOffset());
        final BlockState posUpBlock = index.get(posUp);

        final Optional<ImmutableConnectedDirectionData> data = blockSnapshot.getState().get(ImmutableConnectedDirectionData.class);
        if(!data.isPresent() || !data.get().connectedDirections().exists()) {
            throw new ConnectedDirectionDependencyFactory.NoConnectedDirectionException(this.getClass().getSimpleName()+" configured block state without any "+ ConnectedDirectionData.class.getSimpleName() +" : "+blockSnapshot.getState().getType().getName());
        }

        final Set<Direction> attachedTo = data.get().connectedDirections().get();

        final List<DependencyModel<Vector3i>> dependencies = Arrays.asList(DirectionAdapter.HORIZONTAL).stream()
                .filter(direction -> attachedTo.contains(direction))
                .map(direction -> blockSnapshot.getPosition().add(direction.asBlockOffset()))
                .filter(pos -> {
                    final BlockState sideBlock = index.get(pos);
                    return sideBlock != null && !VineAdapter.isExceptBlockForAttaching(sideBlock.getType());
                })
                .map(BasicDependencyModel::createUniqueDependency)
                .collect(Collectors.toList());

        if(posUpBlock != null && posUpBlock.getType().equals(BlockTypes.VINE)) {
            dependencies.add(BasicDependencyModel.createUniqueDependency(posUp));
        }

        if(dependencies.isEmpty()) {
            return Optional.empty();
        }

        if(dependencies.size() == 1) {
            return Optional.of(dependencies.get(0));
        }

        return Optional.of(new OrDependencyModel<>(dependencies.toArray(new DependencyModel[dependencies.size()])));

    }
}
