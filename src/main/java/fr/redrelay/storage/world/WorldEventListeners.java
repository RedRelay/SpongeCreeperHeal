package fr.redrelay.storage.world;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.world.LoadWorldEvent;
import org.spongepowered.api.event.world.UnloadWorldEvent;

import java.io.IOException;

public class WorldEventListeners {

    private final WorldStorages storages;
    private final Logger logger;

    WorldEventListeners(WorldStorages storages) {
        this.storages = storages;
        this.logger = LoggerFactory.getLogger(WorldEventListeners.class.getSimpleName()+":"+storages.getLabel());
    }

    @Listener
    public void onWorldLoad(LoadWorldEvent e) {
        try {
            storages.registerStorage(e.getTargetWorld());
        } catch (IOException err) {
            logger.error("Unable to open a "+WorldStorage.class.getSimpleName()+" for world "+e.getTargetWorld().getName()+" : data will not be loaded or persisted", err);
        }
    }

    @Listener
    public void onWorldUnload(UnloadWorldEvent e) {
        storages.unregisterStorage(e.getTargetWorld());
    }
}
