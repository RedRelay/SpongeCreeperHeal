package fr.redrelay.spongecreeperheal.chunk;

import fr.redrelay.spongecreeperheal.storage.world.WorldStorages;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.event.world.chunk.LoadChunkEvent;
import org.spongepowered.api.event.world.chunk.UnloadChunkEvent;

/**
 * Register all chunk event related
 */
public class HealableChunksEventListeners {
    private static final HealableChunksEventListeners INSTANCE = new HealableChunksEventListeners();

    private HealableChunksEventListeners() {}

    /**
     * On Chunk load, load HealableChunk from mod database then register it
     * @param e
     */
    @Listener
    public void onChunkLoad(LoadChunkEvent e) {
        WorldStorages.getInstance().get(e.getTargetChunk().getWorld().getName()).ifPresent(worldStorage -> {
            worldStorage.get(e.getTargetChunk().getPosition()).ifPresent(healableChunk -> {
                healableChunk.worldName = e.getTargetChunk().getWorld().getName();
                healableChunk.chunkPos = e.getTargetChunk().getPosition();
                HealableChunks.getInstance().register(e.getTargetChunk().getUniqueId(), healableChunk);
            });
        });
    }

    /**
     * On Chunk unload, save HealableChunk to mod database, then unregister it
     * @param e
     */
    @Listener
    public void onChunkUnload(UnloadChunkEvent e) {
        HealableChunks.getInstance().get(e.getTargetChunk().getUniqueId()).ifPresent(healableChunk -> {
            /* Chunk Save seems not to be implemented so we save chunks when unloaded */
            this.saveChunk(healableChunk);
            HealableChunks.getInstance().unregister(e.getTargetChunk().getUniqueId());
        });
    }

    /**
     * On server stop, save all loaded HealableChunk to mod database, then unregister all of them
     * @param e
     */
    @Listener
    public void onServerStop(GameStoppingServerEvent e) {
        HealableChunks.getInstance().getRawMap().forEach((key, value) -> saveChunk(value));
        HealableChunks.getInstance().clear();
    }

    /**
     * When a HealableChunk is restored, remove it from the database
     * @param chunk
     */
    public void onHealableChunkDone(HealableChunk chunk) {
        WorldStorages.getInstance().get(chunk.getWorldName()).ifPresent(worldStorage -> {
            worldStorage.delete(chunk);
        });
    }

    /**
     * Save a HealableChunk to mod database
     * @param chunk
     */
    private void saveChunk(HealableChunk chunk) {
        WorldStorages.getInstance().get(chunk.getWorldName()).ifPresent(worldStorage -> {
            worldStorage.save(chunk);
        });
    }

    public static HealableChunksEventListeners getInstance() { return INSTANCE; }

}
