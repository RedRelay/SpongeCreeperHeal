package fr.redrelay.spongecreeperheal.factory.explosion;

import fr.redrelay.dependency.DependencyIterator;
import fr.redrelay.dependency.DependencyNode;
import fr.redrelay.spongecreeperheal.accessor.impl.HealableBlockAccessor;
import fr.redrelay.spongecreeperheal.explosion.ChunkedExplosionSnapshot;
import fr.redrelay.spongecreeperheal.factory.dependency.DependencyFactory;
import fr.redrelay.spongecreeperheal.healable.atom.block.HealableBlock;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.event.world.ExplosionEvent;
import org.spongepowered.api.world.BlockChangeFlags;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
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
    public ChunkedExplosionSnapshot build(ExplosionEvent.Detonate e) {

        final HealableBlockAccessor healableBlockAccessor = new HealableBlockAccessor(e);

        final List<DependencyNode<HealableBlock>> dependencyNodes = healableBlockAccessor.getHealableBlocks().stream()
                .map(block -> DependencyFactory.getInstance().createDependencyNode(block, healableBlockAccessor))
                .collect(Collectors.toList());

        accessor.getRegistry().keySet().forEach();

        final DependencyIterator<HealableBlock> it = new DependencyIterator<>(dependencyNodes);
        final Collection<HealableBlock> healables = new LinkedList<>();
        it.forEachRemaining(healables::add);

        return new ChunkedExplosionSnapshot(healables);
    }

    private void removeBlocks(HealableBlockAccessor accessor) {

        accessor.getHealableBlocks().stream().flatMap()

        final List<Location<World>> affectedBlockLocations = e.getAffectedLocations().parallelStream()
                .filter(worldLocation -> worldLocation.getBlockType() != BlockTypes.AIR)
                .collect(Collectors.toList());

        //Remove block to prevent item drop
        affectedBlockLocations.forEach(worldLocation -> worldLocation.setBlockType(BlockTypes.AIR, BlockChangeFlags.NONE));
        //Update blocks physics
        affectedBlockLocations.forEach(worldLocation -> worldLocation.setBlockType(BlockTypes.AIR));
    }

    public static ExplosionSnapshotFactory getInstance() { return INSTANCE; }

}
