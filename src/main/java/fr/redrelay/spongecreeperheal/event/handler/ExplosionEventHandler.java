package fr.redrelay.spongecreeperheal.event.handler;

import fr.redrelay.spongecreeperheal.chunk.ChunkContainerRegistry;
import fr.redrelay.spongecreeperheal.factory.explosion.HealableExplosionFactory;
import fr.redrelay.spongecreeperheal.healable.explosion.HealableExplosion;
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
        final HealableExplosion healableExplosion = HealableExplosionFactory.getInstance().build(e);
        ChunkContainerRegistry.getInstance().add(e.getTargetWorld(), healableExplosion);
    }


    public static ExplosionEventHandler getInstance() { return INSTANCE; }
}
