package fr.redrelay.spongecreeperheal.explosion;

import fr.redrelay.spongecreeperheal.chunk.ChunkContainerRegistry;
import fr.redrelay.spongecreeperheal.factory.explosion.ExplosionSnapshotFactory;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.world.ExplosionEvent;

/**
 * Handle explosion
 */
public class ExplosionEventHandler {
    private static final ExplosionEventHandler INSTANCE = new ExplosionEventHandler();

    private ExplosionEventHandler() {}

    @Listener
    public void onDetonate(ExplosionEvent.Detonate e) {
        final ExplosionSnapshot explosionSnapshot = ExplosionSnapshotFactory.getInstance().build(e);
        ChunkContainerRegistry.getInstance().add(e.getTargetWorld(), explosionSnapshot);
    }


    public static ExplosionEventHandler getInstance() { return INSTANCE; }
}
