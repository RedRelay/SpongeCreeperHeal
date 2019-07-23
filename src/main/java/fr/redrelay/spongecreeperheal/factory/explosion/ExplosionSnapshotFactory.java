package fr.redrelay.spongecreeperheal.factory.explosion;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.dependency.DependencyIterator;
import fr.redrelay.dependency.DependencyNode;
import fr.redrelay.spongecreeperheal.accessor.impl.HealableBlockAccessor;
import fr.redrelay.spongecreeperheal.explosion.ChunkedExplosionSnapshot;
import fr.redrelay.spongecreeperheal.explosion.ExplosionSnapshot;
import fr.redrelay.spongecreeperheal.factory.dependency.DependencyFactory;
import fr.redrelay.spongecreeperheal.healable.ChunkedHealable;
import fr.redrelay.spongecreeperheal.healable.Healable;
import fr.redrelay.spongecreeperheal.healable.atom.HealableAtom;
import fr.redrelay.spongecreeperheal.healable.atom.block.HealableBlock;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.LocatableSnapshot;
import org.spongepowered.api.event.world.ExplosionEvent;
import org.spongepowered.api.world.BlockChangeFlags;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

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
    public ExplosionSnapshot build(ExplosionEvent.Detonate e) {

        final HealableBlockAccessor accessor = new HealableBlockAccessor(e);

        this.removeBlocks(accessor.getHealableBlocks());

        final List<DependencyNode<HealableBlock>> dependencyNodes = createDependencyNodes(accessor);

        final List<HealableBlock> healableBlocks = getSortedHealableBlocks(dependencyNodes);

        final Set<ChunkedExplosionSnapshot> chunkedExplosionSnapshots = createChunkedExplosionSnapshot(healableBlocks);

        return new ExplosionSnapshot(chunkedExplosionSnapshots);
    }

    private void removeBlocks(Collection<? extends HealableBlock> healableBlocks) {

        final List<Location<World>> affectedBlockLocations = healableBlocks.stream()
                .flatMap(a -> a.getBlockSnapshots().values().stream())
                .map(LocatableSnapshot::getLocation)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        //Remove block to prevent item drop
        affectedBlockLocations.forEach(worldLocation -> worldLocation.setBlockType(BlockTypes.AIR, BlockChangeFlags.NONE));
        //Update blocks physics
        affectedBlockLocations.forEach(worldLocation -> worldLocation.setBlockType(BlockTypes.AIR));
    }

    private List<DependencyNode<HealableBlock>> createDependencyNodes(HealableBlockAccessor accessor) {
        return accessor.getHealableBlocks().stream()
                .map(block -> DependencyFactory.getInstance().createDependencyNode(block, accessor))
                .collect(Collectors.toList());
    }

    private List<HealableBlock> getSortedHealableBlocks(List<DependencyNode<HealableBlock>> dependencyNodes) {
        final DependencyIterator<HealableBlock> it = new DependencyIterator<>(dependencyNodes);
        final List<HealableBlock> healables = new LinkedList<>();
        it.forEachRemaining(healables::add);
        return healables;
    }

    private Set<ChunkedExplosionSnapshot> createChunkedExplosionSnapshot(List<? extends HealableAtom> healables) {
        //TODO : ScheduleService
        final Map<Vector3i, List<HealableAtom>> chunkedHealables = healables.parallelStream()
                .flatMap(healable -> healable.getChunks().parallelStream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.toList())));
        return null;
    }

    public static ExplosionSnapshotFactory getInstance() { return INSTANCE; }

}
