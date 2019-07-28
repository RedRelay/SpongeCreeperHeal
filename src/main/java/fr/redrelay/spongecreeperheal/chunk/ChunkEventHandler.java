package fr.redrelay.spongecreeperheal.chunk;

import fr.redrelay.spongecreeperheal.storage.world.WorldStorages;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.event.world.chunk.LoadChunkEvent;
import org.spongepowered.api.event.world.chunk.UnloadChunkEvent;

/**
 * Register all chunk event related
 */
public class ChunkEventHandler {
    private static final ChunkEventHandler INSTANCE = new ChunkEventHandler();

    private ChunkEventHandler() {}

    /**
     * On Chunk load, load ChunkContainer from mod database then register it
     * @param e
     */
    @Listener
    public void onChunkLoad(LoadChunkEvent e) {
        WorldStorages.getInstance().get(e.getTargetChunk().getWorld().getName()).ifPresent(worldStorage -> {
            worldStorage.get(e.getTargetChunk()).ifPresent(healableChunk -> {
                ChunkContainerRegistry.getInstance().register(e.getTargetChunk().getUniqueId(), healableChunk);
            });
        });
    }

    /**
     * On Chunk unload, save ChunkContainer to mod database, then unregister it
     * @param e
     */
    @Listener
    public void onChunkUnload(UnloadChunkEvent e) {
        ChunkContainerRegistry.getInstance().get(e.getTargetChunk().getUniqueId()).ifPresent(healableChunk -> {
            /* Chunk Save seems not to be implemented so we save chunks when unloaded */
            this.saveChunk(healableChunk);
            ChunkContainerRegistry.getInstance().unregister(e.getTargetChunk().getUniqueId());
        });
    }

    /**
     * On server stop, save all loaded ChunkContainer to mod database, then unregister all of them
     * @param e
     */
    @Listener
    public void onServerStop(GameStoppingServerEvent e) {
        ChunkContainerRegistry.getInstance().getRawMap().forEach((key, value) -> saveChunk(value));
        ChunkContainerRegistry.getInstance().clear();
    }

    /**
     * TODO : Keep empty ChunkContainer in memory, flush them only when saving
     * When a ChunkContainer is restored, remove it from the database
     * @param chunk
     */
    public void onHealableChunkDone(ChunkContainer chunk) {
        WorldStorages.getInstance().get(chunk.getWorld().getName()).ifPresent(worldStorage -> {
            worldStorage.delete(chunk);
        });
    }

    /**
     * Save a ChunkContainer to mod database
     * @param chunk
     */
    private void saveChunk(ChunkContainer chunk) {
        WorldStorages.getInstance().get(chunk.getWorld().getName()).ifPresent(worldStorage -> {
            worldStorage.save(chunk);
        });
    }

    public static ChunkEventHandler getInstance() { return INSTANCE; }

}
