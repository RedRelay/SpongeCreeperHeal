package fr.redrelay.spongecreeperheal.chunk;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.spongecreeperheal.SpongeCreeperHeal;
import fr.redrelay.spongecreeperheal.chunk.component.HealableExplosion;
import org.slf4j.Logger;
import org.spongepowered.api.data.*;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * HealableChunk contains a list of HealableExplosion to be restored
 * It represents all explosions for a chunk
 */
public class HealableChunk implements DataSerializable {

    private static class Keys {
        final static DataQuery EXPLOSION = DataQuery.of("explosions");
    }

    private static Logger logger = SpongeCreeperHeal.getLogger();

    private final List<HealableExplosion> explosions = new LinkedList<>();
    protected String worldName;
    protected Vector3i chunkPos;

    private HealableChunk(Collection<HealableExplosion> explosions) {
        if(explosions.isEmpty()) {
            throw new RuntimeException(HealableChunk.class.getSimpleName()+" must contains at least one explosion to heal");
        }
        this.explosions.addAll(explosions);
    }

    protected HealableChunk(String worldName, Vector3i chunkPos, HealableExplosion explosion) {
        this.worldName = worldName;
        this.chunkPos = chunkPos;
        this.explosions.add(explosion);
    }

    /**
     * Gets world name where the chunk is located
     * @return
     */
    public String getWorldName() {
        return worldName;
    }

    /**
     * Gets where is located the chunk
     * @return
     */
    public Vector3i getChunkPos() {
        return chunkPos;
    }

    /**
     * Due to serialization and deserialization mechanism
     * We cannot always use world name and chunk position while in constructor
     * This method helps to know if HealableChunk is correctly attached to a world
     * @return
     */
    public boolean isLinkedToMinecraftCoord() {
        return worldName != null && chunkPos != null;
    }

    public List<HealableExplosion> getExplosions() {
        return explosions;
    }

    public void tick() {
        explosions.forEach(HealableExplosion::tick);
        explosions.removeAll(explosions.parallelStream().filter(e -> e.getEntries().isEmpty()).collect(Collectors.toList()));
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

    @Override
    public DataContainer toContainer() {
        final DataContainer data = DataContainer.createNew();
        data.set(Queries.CONTENT_VERSION, getContentVersion());
        data.set(Keys.EXPLOSION, explosions);
        return data;
    }

    /**
     * Used to build HealableChunk
     */
    public static class HealableChunkBuilder extends AbstractDataBuilder<HealableChunk> {

        public HealableChunkBuilder() {
            super(HealableChunk.class, 0);
        }

        @Override
        protected Optional<HealableChunk> buildContent(DataView container) throws InvalidDataException {
            final Optional<List<HealableExplosion>> opt = container.getSerializableList(Keys.EXPLOSION, HealableExplosion.class);
            if(!opt.isPresent()) {
                logger.error("Found a {} data without explosions ... skipping.", HealableChunk.class.getSimpleName());
                return Optional.empty();
            }
            return Optional.of(new HealableChunk(opt.get()));
        }
    }
}
