package fr.redrelay.spongecreeperheal.storage.world;

import fr.redrelay.spongecreeperheal.SpongeCreeperHeal;
import fr.redrelay.spongecreeperheal.chunk.ChunkContainerRegistry;
import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.world.LoadWorldEvent;
import org.spongepowered.api.event.world.SaveWorldEvent;
import org.spongepowered.api.event.world.UnloadWorldEvent;

import java.io.IOException;

public class WorldStoragesEventListeners {
    private static final WorldStoragesEventListeners INSTANCE = new WorldStoragesEventListeners();

    private Logger logger = SpongeCreeperHeal.getLogger();

    private WorldStoragesEventListeners() {};

    @Listener
    public void onWorldLoad(LoadWorldEvent e) {
        try {
            WorldStorages.getInstance().registerStorage(e.getTargetWorld());
        } catch (IOException err) {
            logger.error("Unable to open a WorldStorage for world "+e.getTargetWorld().getName()+" : data will not be persisted", err);
        }
    }

    @Listener
    public void onWorldUnload(UnloadWorldEvent e) {
        WorldStorages.getInstance().unregisterStorage(e.getTargetWorld());
    }

    @Listener
    public void onWorldSave(SaveWorldEvent.Post e) {
        e.getTargetWorld().getLoadedChunks().forEach(chunk -> {
            ChunkContainerRegistry.getInstance().get(chunk.getUniqueId()).ifPresent(healableChunk -> {
                WorldStorages.getInstance().get(e.getTargetWorld().getName()).ifPresent(storage -> {
                    storage.save(healableChunk);
                });
            });
        });
    }

    public static WorldStoragesEventListeners getInstance() { return INSTANCE; }
}
