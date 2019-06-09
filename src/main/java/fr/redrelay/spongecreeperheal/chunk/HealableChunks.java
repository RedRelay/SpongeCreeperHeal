package fr.redrelay.spongecreeperheal.chunk;

import fr.redrelay.spongecreeperheal.SpongeCreeperHeal;
import fr.redrelay.spongecreeperheal.snapshot.ExplosionSnapshot;
import org.slf4j.Logger;
import org.spongepowered.api.world.Chunk;

import java.util.*;
import java.util.stream.Collectors;

/**
 * All HealableChunk loaded are registered here
 * It is a singleton
 */
public class HealableChunks {
    private final static HealableChunks INSTANCE = new HealableChunks();

    private final Map<UUID, HealableChunk> map = new HashMap<>();

    private final Logger logger = SpongeCreeperHeal.getLogger();

    private HealableChunks(){};

    /**
     * Returns a loaded HealableChunk by its UUID
     * @param chunkId
     * @return HealableChunk or an Optional empty if chunk is not currently loaded
     * or HealableChunk does not exist for this chunk
     */
    public Optional<HealableChunk> get(UUID chunkId) {
        return Optional.ofNullable(map.get(chunkId));
    }


    /**
     * Adds a new ExplosionSnapshot to a Chunk
     * If Chunk already contains a HealableChunk it is added to it
     * Else it is created then registered
     * @param chunk
     * @param explosion
     */
    public void add(Chunk chunk, ExplosionSnapshot explosion) {
        final Optional<HealableChunk> healableChunk = this.get(chunk.getUniqueId());
        if(healableChunk.isPresent()) {
            logger.debug("Adding a new explosion to an existing {} {}",HealableChunk.class.getSimpleName(), healableChunk.get().getChunkPos().toString());
            healableChunk.get().getExplosions().add(explosion);
        }else {
            logger.debug("Adding a new explosion to a new {} {}", HealableChunk.class.getSimpleName(), chunk.getPosition().toString());
            register(chunk.getUniqueId(), new HealableChunk(chunk.getWorld().getName(), chunk.getPosition(), explosion));
        }
    }

    /**
     * Registers a HealableChunk for the chunk UUID
     * @param id UUID of chunk where the HealableChunk is
     * @param chunk
     * @throws RuntimeException if HealableChunk to register is not correctly attached to the world @see HealableChunk#isLinkedToMinecraftCoord
     */
    protected void register(UUID id, HealableChunk chunk) {
        if(!chunk.isLinkedToMinecraftCoord()) {
            throw new RuntimeException(HealableChunk.class.getSimpleName()+" \""+id+"\" is not linked to Minecraft coordinates");
        }
        logger.info("Registering {} {}", HealableChunk.class.getSimpleName(), id);
        map.put(id, chunk);
    }

    /**
     * Unregister a loaded HealableChunk attached to a chunk
     * If not HealableChunk are currently registred, nothing append
     * @param id - the Chunk UUID
     */
    protected void unregister(UUID id) {
        logger.info("Unregistering {} {} ", HealableChunk.class.getSimpleName(), id);
        map.remove(id);
    }

    /**
     * Propagate tick to all loaded HealableChunk,
     * Then remove empty those
     */
    public void tick() {
        map.values().forEach(HealableChunk::tick);
        map.entrySet().parallelStream()
                .filter(entry -> entry.getValue().getExplosions().isEmpty())
                .collect(Collectors.toList())
                .forEach(entry -> {
                    unregister(entry.getKey());
                    HealableChunksEventListeners.getInstance().onHealableChunkDone(entry.getValue());
                });
    }

    /**
     * Unregister all HealableChunk
     */
    protected void clear() {
        logger.info("Clearing HealableChunks");
        map.clear();
    }

    /**
     * Returns an unmodifiable map of all loaded HealableChuik
     * @return an unmodifiable map
     */
    protected Map<UUID, HealableChunk> getRawMap() {
        return Collections.unmodifiableMap(map);
    }


    public static HealableChunks getInstance() { return INSTANCE; }
}
