package fr.redrelay.spongecreeperheal.explosion;

import fr.redrelay.spongecreeperheal.chunk.ChunkContainerRegistry;
import fr.redrelay.spongecreeperheal.factory.explosion.ExplosionSnapshotFactory;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.world.ExplosionEvent;
import org.spongepowered.api.world.Chunk;

import java.util.Optional;

/**
 * Handle explosion
 */
public class ExplosionEventHandler {
    private static final ExplosionEventHandler INSTANCE = new ExplosionEventHandler();

    private ExplosionEventHandler() {}

    @Listener
    public void onDetonate(ExplosionEvent.Detonate e) {

        final ExplosionSnapshot explosionSnapshot = ExplosionSnapshotFactory.getInstance().build(e);

        explosionSnapshot.getChunks().stream().map(explosionSnapshot::getChunkedExplosion).forEach(optChunkedExplosion -> {
            if(!optChunkedExplosion.isPresent()) {
                throw new RuntimeException(ExplosionSnapshotFactory.class.getSimpleName()+" built an "+ExplosionSnapshot.class.getSimpleName()+" with an unregistered "+ChunkedExplosionSnapshot.class.getSimpleName());
            }

            ChunkContainerRegistry.getInstance().add(e.getTargetWorld(), optChunkedExplosion.get());
        });

    }


    public static ExplosionEventHandler getInstance() { return INSTANCE; }
}
