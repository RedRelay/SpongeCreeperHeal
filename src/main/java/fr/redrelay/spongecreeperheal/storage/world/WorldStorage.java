package fr.redrelay.spongecreeperheal.storage.world;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.spongecreeperheal.SpongeCreeperHeal;
import fr.redrelay.spongecreeperheal.chunk.HealableChunk;
import fr.redrelay.spongecreeperheal.storage.LevelDB;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.world.World;

import java.io.IOException;
import java.util.Optional;

public class WorldStorage {

    private final String worldName;
    private final LevelDB db;

    private final Logger logger = SpongeCreeperHeal.getLogger();

    protected WorldStorage(World world) throws IOException {
        this.worldName = world.getName();
        logger.info("Openning spongecreeperheal:WorldStorage for world \"{}\"", worldName);
        this.db = new LevelDB(world.getDirectory().resolve("spongecreeperheal.db").toFile());
    }

    public Optional<HealableChunk> get(Vector3i chunkPos) {
        final Optional<DataContainer> opt = db.get(chunkPos);
        if(!opt.isPresent()) {
            return Optional.empty();
        }
        return Sponge.getDataManager().deserialize(HealableChunk.class, opt.get());
    }

    public void save(HealableChunk chunk) {
        db.save(chunk.getChunkPos(), chunk.toContainer());
    }

    public void delete(HealableChunk chunk) {
        db.delete(chunk.getChunkPos());
    }

    public void close() {
        logger.info("Closing spongecreeperheal:WorldStorage for world \"{}\"", worldName);
        db.close();
    }
}
