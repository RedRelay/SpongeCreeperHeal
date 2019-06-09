package fr.redrelay.spongecreeperheal.engine.dependency.factory;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.dependency.model.BasicDependencyModel;
import fr.redrelay.dependency.model.DependencyModel;
import fr.redrelay.dependency.model.OrDependencyModel;
import fr.redrelay.spongecreeperheal.adapter.DirectionAdapter;
import fr.redrelay.spongecreeperheal.engine.dependency.rule.DependencyRule;
import net.minecraft.block.Block;
import net.minecraft.block.BlockVine;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableConnectedDirectionData;
import org.spongepowered.api.util.Direction;

import java.util.*;
import java.util.stream.Collectors;

public class VineDependencyFactory extends AbstractDependencyFactory {

    private final static class VineAdapter extends BlockVine {

        private VineAdapter() {}

        protected static boolean isExceptBlockForAttaching(BlockType block) {
            return BlockVine.isExceptBlockForAttaching((Block)block);
        }
    }

    public VineDependencyFactory(DependencyRule rule) {
        super(rule);
    }

    @Override
    public Optional<DependencyModel<Vector3i>> build(BlockSnapshot blockSnapshot, Map<Vector3i, BlockState> index) {
        final Vector3i posUp = blockSnapshot.getPosition().add(Direction.UP.asBlockOffset());
        final Optional<BlockState> posUpBlock = Optional.ofNullable(index.get(posUp));

        final Optional<ImmutableConnectedDirectionData> data = blockSnapshot.getState().get(ImmutableConnectedDirectionData.class);
        if(!data.isPresent() || !data.get().connectedDirections().exists()) {
            throw new ConnectedDirectionDependencyFactory.NoConnectedDirectionException(this.getClass().getSimpleName()+" configured block state without any ConnectedDirectionData : "+blockSnapshot.getState().getType().getName());
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

        if(posUpBlock.isPresent() && posUpBlock.get().getType().equals(BlockTypes.VINE)) {
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
