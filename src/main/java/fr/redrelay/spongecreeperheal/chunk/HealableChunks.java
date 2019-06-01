package fr.redrelay.spongecreeperheal.chunk;

import fr.redrelay.spongecreeperheal.SpongeCreeperHeal;
import fr.redrelay.spongecreeperheal.chunk.component.HealableExplosion;
import org.slf4j.Logger;
import org.spongepowered.api.world.Chunk;

import java.util.*;
import java.util.stream.Collectors;

public class HealableChunks {
    private final static HealableChunks INSTANCE = new HealableChunks();

    private final Map<UUID, HealableChunk> map = new HashMap<>();

    private Logger logger = SpongeCreeperHeal.getLogger();

    private HealableChunks(){};

    public Optional<HealableChunk> get(UUID chunkId) {
        return Optional.ofNullable(map.get(chunkId));
    }

    public void add(Chunk chunk, HealableExplosion explosion) {
        final Optional<HealableChunk> healableChunk = this.get(chunk.getUniqueId());
        if(healableChunk.isPresent()) {
            logger.debug("Adding a new explosion to an existing chunk "+healableChunk.get().getChunkPos().toString());
            healableChunk.get().getExplosions().add(explosion);
        }else {
            logger.debug("Adding a new explosion to a new HealableChunk "+chunk.getPosition().toString());
            register(chunk.getUniqueId(), new HealableChunk(chunk.getWorld().getName(), chunk.getPosition(), explosion));
        }
    }

    protected void register(UUID id, HealableChunk chunk) {
        if(!chunk.isLinkedToMinecraftCoord()) {
            throw new RuntimeException("HealableChunk \""+id+"\" is not linked to Minecraft coordinates");
        }
        logger.info("Registering HealableChunk "+id);
        map.put(id, chunk);
    }
    protected void unregister(UUID id) {
        logger.info("Unregistering HealableChunk "+id);
        map.remove(id);
    }

    public void tick() {
        map.values().forEach(HealableChunk::tick);
        map.entrySet().parallelStream()
                .filter(entry -> entry.getValue().getExplosions().isEmpty())
                .collect(Collectors.toList())
                .forEach(entry -> {
                    map.remove(entry.getKey());
                    HealableChunksEventListeners.getInstance().onHealableChunkDone(entry.getValue());
                });
    }

    protected void clear() {
        logger.info("Clearing HealableChunks.");
        map.clear();
    }

    protected Map<UUID, HealableChunk> getRawMap() {
        return Collections.unmodifiableMap(map);
    }


    public static HealableChunks getInstance() { return INSTANCE; }
}
