package fr.redrelay.storage.world;

import fr.redrelay.storage.StorageKeyFactory;
import fr.redrelay.storage.data.container.ChunkDataContainer;
import fr.redrelay.storage.database.LevelDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.World;

import java.io.IOException;
import java.util.Optional;

public class WorldStorage {

    private final String label;
    private final World world;
    private final LevelDB db;

    private final Logger logger;

    WorldStorage(String label, World world) throws IOException {
        this.label = label;
        this.world = world;
        this.logger = LoggerFactory.getLogger(WorldStorage.class.getSimpleName()+":"+label);
        logger.info("Opening {} for world \"{}\"", WorldStorage.class.getSimpleName(), world);
        this.db = new LevelDB(world.getDirectory().resolve(label+".db").toFile());
    }

    public <T extends DataSerializable> Optional<T> get(Chunk chunk, Class<T> clazz) throws IOException {
        final Optional<DataContainer> optDataContainer = db.get(StorageKeyFactory.getInstance().getChunkKey(chunk));

        if(!optDataContainer.isPresent()) {
            return Optional.empty();
        }

        final ChunkDataContainer data = new ChunkDataContainer(world, chunk, optDataContainer.get());

        return Sponge.getDataManager().deserialize(clazz, data);
    }

    public <T extends  DataSerializable> void save(Chunk chunk, T value) throws IOException {
        db.save(StorageKeyFactory.getInstance().getChunkKey(chunk), value.toContainer());
    }

    public void delete(Chunk chunk) {
        db.delete(StorageKeyFactory.getInstance().getChunkKey(chunk));
    }

    public void close() throws IOException {
        logger.info("Closing {} {} for world \"{}\"", label, WorldStorage.class.getSimpleName(), world.getName());
        db.close();
    }

    public String getLabel() {
        return label;
    }

    World getWorld() {
        return world;
    }
}
