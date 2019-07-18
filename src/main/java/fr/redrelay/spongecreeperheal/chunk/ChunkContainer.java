package fr.redrelay.spongecreeperheal.chunk;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.spongecreeperheal.SpongeCreeperHeal;
import fr.redrelay.spongecreeperheal.explosion.ExplosionSnapshot;
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
 * ChunkContainer contains a list of ExplosionSnapshot to be restored
 * It represents all explosions for a chunk
 */
public class ChunkContainer implements DataSerializable {

    private static class Keys {
        final static DataQuery EXPLOSION = DataQuery.of("explosions");
    }

    private static Logger logger = SpongeCreeperHeal.getLogger();

    private final List<ExplosionSnapshot> explosions = new LinkedList<>();
    protected String worldName;
    protected Vector3i chunkPos;

    private ChunkContainer(Collection<ExplosionSnapshot> explosions) {
        if(explosions.isEmpty()) {
            throw new RuntimeException(ChunkContainer.class.getSimpleName()+" must contains at least one explosion to heal");
        }
        this.explosions.addAll(explosions);
    }

    protected ChunkContainer(String worldName, Vector3i chunkPos, ExplosionSnapshot explosion) {
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
     * This method helps to know if ChunkContainer is correctly attached to a world
     * @return
     */
    public boolean isLinkedToMinecraftCoord() {
        return worldName != null && chunkPos != null;
    }

    public List<ExplosionSnapshot> getExplosions() {
        return explosions;
    }

    public void tick() {
        explosions.forEach(ExplosionSnapshot::tick);
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

    public static class Builder {


        public ChunkContainer build() {}
    }

    /**
     * Used to provide ChunkContainer
     */
    public static class DataBuilder extends AbstractDataBuilder<ChunkContainer> {

        public DataBuilder() {
            super(ChunkContainer.class, 0);
        }

        @Override
        protected Optional<ChunkContainer> buildContent(DataView container) throws InvalidDataException {
            final Optional<List<ExplosionSnapshot>> opt = container.getSerializableList(Keys.EXPLOSION, ExplosionSnapshot.class);
            if(!opt.isPresent()) {
                logger.error("Found a {} data without explosions ... skipping.", ChunkContainer.class.getSimpleName());
                return Optional.empty();
            }
            return Optional.of(new ChunkContainer(opt.get()));
        }
    }
}
