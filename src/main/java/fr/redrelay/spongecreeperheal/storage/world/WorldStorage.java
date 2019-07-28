package fr.redrelay.spongecreeperheal.storage.world;

import fr.redrelay.spongecreeperheal.SpongeCreeperHeal;
import fr.redrelay.spongecreeperheal.chunk.ChunkContainer;
import fr.redrelay.spongecreeperheal.data.chunk.ChunkContainerBuilder;
import fr.redrelay.spongecreeperheal.storage.LevelDB;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.World;

import java.io.IOException;
import java.util.Optional;

public class WorldStorage {

    private final World world;
    private final LevelDB db;

    private final Logger logger = SpongeCreeperHeal.getLogger();

    protected WorldStorage(World world) throws IOException {
        this.world = world;
        logger.info("Openning spongecreeperheal:WorldStorage for world \"{}\"", world);
        this.db = new LevelDB(world.getDirectory().resolve("spongecreeperheal.db").toFile());
    }

    public Optional<ChunkContainer> get(Chunk chunk) {
        final Optional<DataContainer> opt = db.get(chunk.getPosition());
        if(!opt.isPresent()) {
            return Optional.empty();
        }
        final Optional<ChunkContainerBuilder> data = Sponge.getDataManager().deserialize(ChunkContainerBuilder.class, opt.get());
        if(!data.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(new ChunkContainer(world,chunk,data.get()));
    }

    public void save(ChunkContainer chunkContainer) {
        db.save(chunkContainer.getChunk().getPosition(), chunkContainer.toContainer());
    }

    public void delete(ChunkContainer chunkContainer) {
        db.delete(chunkContainer.getChunk().getPosition());
    }

    public void close() {
        logger.info("Closing spongecreeperheal:WorldStorage for world \"{}\"", world);
        db.close();
    }
}
