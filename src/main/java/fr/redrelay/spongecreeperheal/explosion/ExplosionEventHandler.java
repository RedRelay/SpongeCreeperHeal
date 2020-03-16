package fr.redrelay.spongecreeperheal.explosion;

import fr.redrelay.spongecreeperheal.chunk.ChunkContainerRegistry;
import fr.redrelay.spongecreeperheal.healable.ChunkedHealable;
import fr.redrelay.spongecreeperheal.healable.Healable;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.world.ExplosionEvent;
import org.spongepowered.api.world.BlockChangeFlags;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handle explosion
 */
public class ExplosionEventHandler {

    @Listener
    public void onDetonate(ExplosionEvent.Detonate e) {
        final List<BlockSnapshot> blocks = e.getAffectedLocations().stream()
                .filter(worldLocation -> worldLocation.getBlockType() != BlockTypes.AIR)
                .map( worldLocation -> {
                    //Save all block data in explosion, necessary to restore the block later
                    final BlockSnapshot blockSnapshot = worldLocation.createSnapshot();

                    return blockSnapshot;

                }).collect(Collectors.toList());

        final LinkedList<Healable> healables = ExplosionSnapshotFactory.getInstance().build(blocks);

        this.removeBlocks(blocks);

        //TODO : ScheduleService
        healables.parallelStream()
                .flatMap(healable -> healable.split().parallelStream())
                .collect(Collectors.groupingBy(ChunkedHealable::getChunkPosition, Collectors.toCollection(LinkedList::new)))
                .forEach((chunkPos, chunkedHealables) -> ChunkContainerRegistry.getInstance().add(e.getTargetWorld().getChunk(chunkPos).get(), new ExplosionSnapshot(chunkedHealables)));
    }

    private void removeBlocks(List<BlockSnapshot> blocks) {
        //Remove block to prevent item drop
        blocks.forEach(block -> block.getLocation().ifPresent(world -> world.setBlockType(BlockTypes.AIR, BlockChangeFlags.NONE)));
        //Update block
        blocks.forEach(block -> block.getLocation().ifPresent(world -> world.setBlockType(BlockTypes.AIR)));
    }
}
