package fr.redrelay.spongecreeperheal.factory.explosion;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.dependency.DependencyIterator;
import fr.redrelay.dependency.DependencyNode;
import fr.redrelay.spongecreeperheal.factory.dependency.DependencyFactory;
import fr.redrelay.spongecreeperheal.factory.healable.block.HealableBlockFactory;
import fr.redrelay.spongecreeperheal.healable.atom.block.HealableBlock;
import fr.redrelay.spongecreeperheal.healable.Healable;
import fr.redrelay.spongecreeperheal.healable.atom.block.impl.SimpleHealableBlock;
import fr.redrelay.spongecreeperheal.registry.accessor.BlockStateAccessor;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.LocatableSnapshot;
import org.spongepowered.api.event.world.ExplosionEvent;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * All dependency related stuff are here
 * Rules registering
 * DependencyProvider registering
 * Sorting BlockSnapshot
 */
public class ExplosionSnapshotFactory {
    private static final ExplosionSnapshotFactory INSTANCE = new ExplosionSnapshotFactory();

    private ExplosionSnapshotFactory() {}


    /**
     * Creates a list of Healable for a collection of BlockSnapshot
     * @param e
     * @return
     */
    public LinkedList<Healable> build(ExplosionEvent.Detonate e) {

        final BlockStateAccessor blockStateAccessor = new BlockStateAccessor(e);

        final List<BlockSnapshot> blocks = e.getAffectedLocations().stream()
                .filter(worldLocation -> worldLocation.getBlockType() != BlockTypes.AIR)
                .map( worldLocation -> {
                    //Save all block data in explosion, necessary to restore the block later
                    final BlockSnapshot blockSnapshot = worldLocation.createSnapshot();
                    return blockSnapshot;

                }).collect(Collectors.toList());

        final Collection<HealableBlock> healableBlocks = blocks.stream().collect(HashMap<Vector3i, HealableBlock>::new, (healableBlockIndex, blockSnapshot) -> {
            if(healableBlockIndex.containsKey(blockSnapshot.getPosition())) return;
            final HealableBlock healableBlock = HealableBlockFactory.getInstance().provide(blockSnapshot, blockStateAccessor);
            healableBlockIndex.putAll(healableBlock.getBlockSnapshots().stream().collect(Collectors.toMap(LocatableSnapshot::getPosition, (pos) -> healableBlock)));
        }, Map::putAll).values();


        //Convert BlockSnapshot collection to a list of dependency node
        final List<DependencyNode<Vector3i>> nodes = entries.parallelStream()
                .map(entry -> DependencyFactory.getInstance().provide(entry, blockStateAccessor))
                .collect(Collectors.toList());

        final DependencyIterator<Vector3i> it = new DependencyIterator<>(nodes);
        final LinkedList<Healable> healables = new LinkedList<>();
        it.forEachRemaining(pos -> healables.add(new SimpleHealableBlock(indexedBlockSnapshot.get(pos))));

        return healables;
    }

    public static ExplosionSnapshotFactory getInstance() { return INSTANCE; }

}
