package fr.redrelay.spongecreeperheal.factory.explosion;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.dependency.DependencyIterator;
import fr.redrelay.dependency.DependencyNode;
import fr.redrelay.spongecreeperheal.accessor.impl.HealableBlockAccessor;
import fr.redrelay.spongecreeperheal.factory.dependency.DependencyFactory;
import fr.redrelay.spongecreeperheal.healable.atom.HealableAtom;
import fr.redrelay.spongecreeperheal.healable.atom.block.HealableBlock;
import fr.redrelay.spongecreeperheal.healable.explosion.ChunkedHealableExplosion;
import fr.redrelay.spongecreeperheal.healable.explosion.HealableExplosion;
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
public class HealableExplosionFactory {
    private static final HealableExplosionFactory INSTANCE = new HealableExplosionFactory();

    private HealableExplosionFactory() {}


    /**
     * Creates a list of Healable for a collection of BlockSnapshot
     * @param e
     * @return
     */
    public HealableExplosion build(ExplosionEvent.Detonate e) {

        final HealableBlockAccessor accessor = new HealableBlockAccessor(e);

        this.removeBlocks(accessor.getHealableBlocks());

        final List<DependencyNode<HealableBlock>> dependencyNodes = createDependencyNodes(accessor);

        final List<HealableBlock> healableBlocks = getSortedHealableBlocks(dependencyNodes);

        //TODO : ScheduleService

        final Set<ChunkedHealableExplosion> chunkedHealableExplosions = createChunkedExplosionSnapshot(healableBlocks);

        return new HealableExplosion(chunkedHealableExplosions);
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


    private Set<ChunkedHealableExplosion> createChunkedExplosionSnapshot(List<? extends HealableAtom> healables) {
        final Map<Vector3i, List<HealableAtom>> chunkedHealables = healables.stream()
                .map(healable -> healable.getChunks().stream().collect(Collectors.toMap(Function.identity(), chunk -> healable)))
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.mapping(Map.Entry::getValue, Collectors.toList())));

        return chunkedHealables.entrySet().parallelStream().map(entry -> new ChunkedHealableExplosion(entry.getKey(), entry.getValue())).collect(Collectors.toSet());
    }

    public static HealableExplosionFactory getInstance() { return INSTANCE; }

}
