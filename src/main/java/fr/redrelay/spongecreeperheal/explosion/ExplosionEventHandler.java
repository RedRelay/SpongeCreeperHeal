package fr.redrelay.spongecreeperheal.explosion;

import fr.redrelay.spongecreeperheal.chunk.ChunkContainerRegistry;
import fr.redrelay.spongecreeperheal.factory.explosion.ExplosionSnapshotFactory;
import fr.redrelay.spongecreeperheal.healable.ChunkedHealable;
import fr.redrelay.spongecreeperheal.healable.Healable;
import fr.redrelay.spongecreeperheal.registry.accessor.BlockStateAccessor;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.world.ExplosionEvent;
import org.spongepowered.api.world.BlockChangeFlags;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handle explosion
 */
public class ExplosionEventHandler {
    private static final ExplosionEventHandler INSTANCE = new ExplosionEventHandler();

    private ExplosionEventHandler() {}

    @Listener
    public void onDetonate(ExplosionEvent.Detonate e) {



        final List<BlockSnapshot> blocks = e.getAffectedLocations().stream()
                .filter(worldLocation -> worldLocation.getBlockType() != BlockTypes.AIR)
                .map( worldLocation -> {
                    //Save all block data in explosion, necessary to restore the block later
                    final BlockSnapshot blockSnapshot = worldLocation.createSnapshot();

                    return blockSnapshot;

                }).collect(Collectors.toList());

        final LinkedList<Healable> healables = ExplosionSnapshotFactory.getInstance().build(e);

        this.removeBlocks(e);

        //TODO : ScheduleService
        healables.parallelStream()
                .flatMap(healable -> healable.split().parallelStream())
                .collect(Collectors.groupingBy(ChunkedHealable::getChunkPosition, Collectors.toCollection(LinkedList::new)))
                .forEach((chunkPos, chunkedHealables) -> ChunkContainerRegistry.getInstance().add(e.getTargetWorld().getChunk(chunkPos).get(), new ExplosionSnapshot(chunkedHealables)));
    }

    private void removeBlocks(ExplosionEvent.Detonate e) {

        final List<Location<World>> affectedBlockLocations = e.getAffectedLocations().parallelStream()
                .filter(worldLocation -> worldLocation.getBlockType() != BlockTypes.AIR)
                .collect(Collectors.toList());

        //Remove block to prevent item drop
        affectedBlockLocations.forEach(worldLocation -> worldLocation.setBlockType(BlockTypes.AIR, BlockChangeFlags.NONE));
        //Update blocks physics
        affectedBlockLocations.forEach(worldLocation -> worldLocation.setBlockType(BlockTypes.AIR));
    }


    public static ExplosionEventHandler getInstance() { return INSTANCE; }
}
