package fr.redrelay.spongecreeperheal.dependency;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.dependency.DependencyIterator;
import fr.redrelay.dependency.DependencyNode;
import fr.redrelay.dependency.model.DependencyModel;
import fr.redrelay.dependency.model.NoDependencyModel;
import fr.redrelay.spongecreeperheal.SpongeCreeperHeal;
import fr.redrelay.spongecreeperheal.block.HealableEntry;
import fr.redrelay.spongecreeperheal.dependency.rule.DependencyRule;
import org.slf4j.Logger;
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
 * DependencyFactory registering
 * Sorting BlockSnapshot
 */
public class DependencyEngine {
    private static final DependencyEngine INSTANCE = new DependencyEngine();

    private final Logger logger = SpongeCreeperHeal.getLogger();
    private final Map<BlockType, DependencyFactory> map = new HashMap<>();

    private DependencyEngine() {}


    /**
     * Creates a list of HealableEntry for a collection of BlockSnapshot
     * @param entries
     * @return
     */
    public LinkedList<HealableEntry> build(Collection<BlockSnapshot> entries) {
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
        final LinkedList<HealableEntry> healables = new LinkedList<>();
        it.forEachRemaining(pos -> healables.add(new HealableEntry(indexedBlockSnapshot.get(pos))));

        return healables;
    }

    public Optional<DependencyFactory> getDependencyFactory(BlockType block) {
        return Optional.ofNullable(map.get(block));
    }

    public void register(DependencyRule rule) {
        logger.info("Register dependency rule \"{}\"",rule.getName());
        rule.registerDependencies();
    }

    public void register(BlockType block, DependencyFactory factory) {
        logger.info("Register dependency factory \"{}\" for block \"{}\"",factory.getClass().getName(), block.getName());
        map.put(block, factory);
    }

    public Map<BlockType, DependencyFactory> getDependencyMap() {
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
        final Optional<DependencyFactory> optFactory = getDependencyFactory(entry.getState().getType());
        if(optFactory.isPresent()) {
            final Optional<DependencyModel<Vector3i>> optDependencyModel = optFactory.get().build(entry, indexedBlockStates);
            if(optDependencyModel.isPresent()) {
                return new DependencyNode<>(entry.getPosition(), optDependencyModel.get());
            }

        }

        return new DependencyNode<Vector3i>(entry.getPosition(),NoDependencyModel.getInstance());
    }

    public static DependencyEngine getInstance() { return INSTANCE; }

}
