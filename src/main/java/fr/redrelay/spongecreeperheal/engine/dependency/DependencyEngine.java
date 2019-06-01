package fr.redrelay.spongecreeperheal.engine.dependency;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.dependency.DependencyIterator;
import fr.redrelay.dependency.DependencyNode;
import fr.redrelay.dependency.model.DependencyModel;
import fr.redrelay.dependency.model.NoDependencyModel;
import fr.redrelay.spongecreeperheal.SpongeCreeperHeal;
import fr.redrelay.spongecreeperheal.chunk.component.HealableEntry;
import org.slf4j.Logger;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DependencyEngine {
    private static final DependencyEngine INSTANCE = new DependencyEngine();

    private Logger logger = SpongeCreeperHeal.getLogger();
    private final Map<BlockType, DependencyFactory> map = new HashMap<>();

    private DependencyEngine() {}


    public LinkedList<HealableEntry> build(Collection<BlockSnapshot> entries) {

        final Map<Vector3i, BlockSnapshot> indexedBlockSnapshot = entries.parallelStream()
                .collect(Collectors.toMap(entry -> entry.getPosition(), Function.identity()));

        final Map<Vector3i, BlockState> indexedBlockStates = Collections.unmodifiableMap(entries.parallelStream()
                .collect(Collectors.toMap(entry -> entry.getPosition(), entry -> entry.getState())));

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

    public void register(BlockType block, DependencyFactory factory) {
        logger.info("Register dependency factory \""+factory.getClass().getName()+"\" for block \""+block.getName()+"\"");
        map.put(block, factory);
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
                return new DependencyNode<Vector3i>(entry.getPosition(), optDependencyModel.get());
            }

        }

        return new DependencyNode<Vector3i>(entry.getPosition(),NoDependencyModel.getInstance());
    }

    public static DependencyEngine getInstance() { return INSTANCE; }

}