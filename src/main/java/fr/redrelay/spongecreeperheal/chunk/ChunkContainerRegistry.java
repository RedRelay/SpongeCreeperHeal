package fr.redrelay.spongecreeperheal.chunk;

import fr.redrelay.spongecreeperheal.SpongeCreeperHeal;
import fr.redrelay.spongecreeperheal.explosion.ExplosionSnapshot;
import org.slf4j.Logger;
import org.spongepowered.api.world.Chunk;

import java.util.*;
import java.util.stream.Collectors;

/**
 * All ChunkContainer loaded are registered here
 * It is a singleton
 */
public class ChunkContainerRegistry {
    private final static ChunkContainerRegistry INSTANCE = new ChunkContainerRegistry();

    private final Map<UUID, ChunkContainer> map = new HashMap<>();

    private final Logger logger = SpongeCreeperHeal.getLogger();

    private ChunkContainerRegistry(){}

    /**
     * Returns a loaded ChunkContainer by its UUID
     * @param chunkId
     * @return ChunkContainer or an Optional empty if chunk is not currently loaded
     * or ChunkContainer does not exist for this chunk
     */
    public Optional<ChunkContainer> get(UUID chunkId) {
        return Optional.ofNullable(map.get(chunkId));
    }


    /**
     * Adds a new ExplosionSnapshot to a Chunk
     * If Chunk already contains a ChunkContainer it is added to it
     * Else it is created then registered
     * @param chunk
     * @param explosion
     */
    public void add(Chunk chunk, ExplosionSnapshot explosion) {
        final Optional<ChunkContainer> healableChunk = this.get(chunk.getUniqueId());
        if(healableChunk.isPresent()) {
            logger.debug("Adding a new explosion to an existing {} {}", ChunkContainer.class.getSimpleName(), healableChunk.get().getChunkPos().toString());
            healableChunk.get().getExplosions().add(explosion);
        }else {
            logger.debug("Adding a new explosion to a new {} {}", ChunkContainer.class.getSimpleName(), chunk.getPosition().toString());
            register(chunk.getUniqueId(), new ChunkContainer(chunk.getWorld().getName(), chunk.getPosition(), explosion));
        }
    }

    /**
     * Registers a ChunkContainer for the chunk UUID
     * @param id UUID of chunk where the ChunkContainer is
     * @param chunk
     * @throws RuntimeException if ChunkContainer to register is not correctly attached to the world @see ChunkContainer#isLinkedToMinecraftCoord
     */
    protected void register(UUID id, ChunkContainer chunk) {
        if(!chunk.isLinkedToMinecraftCoord()) {
            throw new RuntimeException(ChunkContainer.class.getSimpleName()+" \""+id+"\" is not linked to Minecraft coordinates");
        }
        logger.info("Registering {} {}", ChunkContainer.class.getSimpleName(), id);
        map.put(id, chunk);
    }

    /**
     * Unregister a loaded ChunkContainer attached to a chunk
     * If not ChunkContainer are currently registred, nothing append
     * @param id - the Chunk UUID
     */
    protected void unregister(UUID id) {
        logger.info("Unregistering {} {} ", ChunkContainer.class.getSimpleName(), id);
        map.remove(id);
    }

    /**
     * Propagate tick to all loaded ChunkContainer,
     * Then remove empty those
     */
    public void tick() {
        map.values().forEach(ChunkContainer::tick);
        map.entrySet().parallelStream()
                .filter(entry -> entry.getValue().getExplosions().isEmpty())
                .collect(Collectors.toList())
                .forEach(entry -> {
                    unregister(entry.getKey());
                    ChunkEventHandler.getInstance().onHealableChunkDone(entry.getValue());
                });
    }

    /**
     * Unregister all ChunkContainer
     */
    protected void clear() {
        logger.info("Clearing ChunkContainerRegistry");
        map.clear();
    }

    /**
     * Returns an unmodifiable map of all loaded HealableChuik
     * @return an unmodifiable map
     */
    protected Map<UUID, ChunkContainer> getRawMap() {
        return Collections.unmodifiableMap(map);
    }


    public static ChunkContainerRegistry getInstance() { return INSTANCE; }
}
