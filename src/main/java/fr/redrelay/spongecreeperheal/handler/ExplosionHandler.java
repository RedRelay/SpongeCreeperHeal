package fr.redrelay.spongecreeperheal.handler;

import fr.redrelay.spongecreeperheal.chunk.HealableChunks;
import fr.redrelay.spongecreeperheal.chunk.component.HealableEntry;
import fr.redrelay.spongecreeperheal.chunk.component.HealableExplosion;
import fr.redrelay.spongecreeperheal.engine.dependency.DependencyEngine;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.world.ExplosionEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ExplosionHandler {
    private static final ExplosionHandler INSTANCE = new ExplosionHandler();

    private ExplosionHandler() {}

    @Listener
    public void onDetonate(ExplosionEvent.Detonate e) {

        final List<BlockSnapshot> blocks = e.getAffectedLocations().stream()
                //.filter(worldLocation -> worldLocation.getLocatableBlock().isPresent())
                .filter(worldLocation -> worldLocation.getBlockType() != BlockTypes.AIR)
                .map( worldLocation -> {
                    //Save all block data in snapshot, necessary to restore the block later
                    final BlockSnapshot blockSnapshot = worldLocation.createSnapshot();
                    //Remove block to prevent item drop
                    worldLocation.setBlock(BlockState.builder().blockType(BlockTypes.AIR).build());

                    return blockSnapshot;

                }).collect(Collectors.toList());

        final LinkedList<HealableEntry> healables = DependencyEngine.getInstance().build(blocks);
        //TODO : ScheduleService
        healables.parallelStream()
                .collect(Collectors.groupingBy(healableEntry -> healableEntry.getBlockSnapshot().getLocation().get().getChunkPosition(), Collectors.toCollection(LinkedList::new)))
                .forEach((chunkPos, healableEntries) -> {
                    HealableChunks.getInstance().add(e.getTargetWorld().getChunk(chunkPos).get(), new HealableExplosion(healableEntries));
                });
    }


    public static ExplosionHandler getInstance() { return INSTANCE; }
}
