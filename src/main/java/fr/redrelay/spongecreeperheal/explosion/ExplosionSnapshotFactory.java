package fr.redrelay.spongecreeperheal.explosion;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.dependency.DependencyIterator;
import fr.redrelay.dependency.DependencyNode;
import fr.redrelay.dependency.model.DependencyModel;
import fr.redrelay.dependency.model.NoDependencyModel;
import fr.redrelay.spongecreeperheal.dependency.DependencyRegistry;
import fr.redrelay.spongecreeperheal.dependency.provider.DependencyProvider;
import fr.redrelay.spongecreeperheal.healable.Healable;
import fr.redrelay.spongecreeperheal.healable.atom.block.impl.SimpleHealableBlock;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.LocatableSnapshot;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * All dependency related stuff are here
 * Rules registering
 * DependencyProvider registering
 * Sorting BlockSnapshot
 */
public class ExplosionSnapshotFactory extends DependencyRegistry {
    private static final ExplosionSnapshotFactory INSTANCE = new ExplosionSnapshotFactory();

    private ExplosionSnapshotFactory() {}


    /**
     * Creates a list of Healable for a collection of BlockSnapshot
     * @param entries
     * @return
     */
    public LinkedList<Healable> build(Collection<BlockSnapshot> entries) {
        final Map<Vector3i, BlockSnapshot> indexedBlockSnapshot = entries.parallelStream()
                .collect(Collectors.toMap(LocatableSnapshot::getPosition, Function.identity()));

        //Create unmodifiable map of position -> blockstate : this is the index for DependencyFactories
        final Map<Vector3i, BlockState> indexedBlockStates = Collections.unmodifiableMap(entries.parallelStream()
                .collect(Collectors.toMap(LocatableSnapshot::getPosition, BlockSnapshot::getState)));

        //Convert BlockSnapshot collection to a list of dependency node
        final List<DependencyNode<Vector3i>> nodes = entries.parallelStream()
                .map(entry -> convertToDependencyNodes(entry, indexedBlockStates))
                .collect(Collectors.toList());

        final DependencyIterator<Vector3i> it = new DependencyIterator<>(nodes);
        final LinkedList<Healable> healables = new LinkedList<>();
        it.forEachRemaining(pos -> healables.add(new SimpleHealableBlock(indexedBlockSnapshot.get(pos))));

        return healables;
    }

    public Map<BlockType, DependencyProvider> getDependencyMap() {
        return Collections.unmodifiableMap(map);
    }

    /**
     * Internally used and necessary to compile.
     * It helps compiler to get type inference
     * @param entry
     * @param indexedBlockStates
     * @return
     */
    private DependencyNode<Vector3i> convertToDependencyNodes(BlockSnapshot entry, Map<Vector3i, BlockState> indexedBlockStates) {
        final Optional<DependencyProvider> optProvider = this.getRegistred(entry.getState().getType());
        if(optProvider.isPresent()) {
            final Optional<DependencyModel<Vector3i>> optDependencyModel = optProvider.get().provide(entry, indexedBlockStates);
            if(optDependencyModel.isPresent()) {
                return new DependencyNode<>(entry.getPosition(), optDependencyModel.get());
            }

        }

        return new DependencyNode<Vector3i>(entry.getPosition(),NoDependencyModel.getInstance());
    }

    public static ExplosionSnapshotFactory getInstance() { return INSTANCE; }

}
