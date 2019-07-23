package fr.redrelay.spongecreeperheal.explosion;

import fr.redrelay.spongecreeperheal.chunk.ChunkContainerRegistry;
import fr.redrelay.spongecreeperheal.factory.explosion.ExplosionSnapshotFactory;
import fr.redrelay.spongecreeperheal.healable.ChunkedHealable;
import fr.redrelay.spongecreeperheal.healable.Healable;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.world.ExplosionEvent;

import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * Handle explosion
 */
public class ExplosionEventHandler {
    private static final ExplosionEventHandler INSTANCE = new ExplosionEventHandler();

    private ExplosionEventHandler() {}

    @Listener
    public void onDetonate(ExplosionEvent.Detonate e) {

        final ExplosionSnapshot explosionSnapshot = ExplosionSnapshotFactory.getInstance().build(e);


        //TODO : ScheduleService
        healables.parallelStream()
                .flatMap(healable -> healable.split().parallelStream())
                .collect(Collectors.groupingBy(ChunkedHealable::getChunkPosition, Collectors.toCollection(LinkedList::new)))
                .forEach((chunkPos, chunkedHealables) -> ChunkContainerRegistry.getInstance().add(e.getTargetWorld().getChunk(chunkPos).get(), new ExplosionSnapshot(chunkedHealables)));
    }


    public static ExplosionEventHandler getInstance() { return INSTANCE; }
}
