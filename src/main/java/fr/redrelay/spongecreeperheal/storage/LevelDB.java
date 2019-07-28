package fr.redrelay.spongecreeperheal.storage;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.spongecreeperheal.SpongeCreeperHeal;
import org.fusesource.leveldbjni.JniDBFactory;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.slf4j.Logger;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.persistence.DataFormats;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class LevelDB {

    private final Logger logger = SpongeCreeperHeal.getLogger();

    private final DB db;

    public LevelDB(File dbFile) throws IOException {
        Options options = new Options();
        options.createIfMissing(true);
        this.db = JniDBFactory.factory.open(dbFile, options);
    }

    public void close() {
        try {
            db.close();
        } catch (IOException e) {
            logger.error("Unable to close {} : {}", LevelDB.class.getSimpleName(), e);
        }
    }

    public void save(Vector3i chunkPos, DataContainer data) {
        try {
            db.put(bytes(chunkPos), bytes(data));
        } catch (IOException e) {
            logger.error("Unable to save {} {} : {}", DataContainer.class.getSimpleName(), data.getName(), e);
        }
    }

    public Optional<DataContainer> get(Vector3i chunkPos) {
        try {
            final byte[] data = db.get(bytes(chunkPos));
            return Optional.of(asDataContainer(data));
        } catch (IOException e) {
            logger.error("Unable to load {} of chunk {} : {}",DataContainer.class.getSimpleName(), chunkPos.toString(), e);
        }
        return Optional.empty();
    }

    public void delete(Vector3i chunkPos) {
        db.delete(bytes(chunkPos));
    }

    private byte[] bytes(Vector3i chunkPos) {
        return new byte[]{
                Integer.valueOf(chunkPos.getX()).byteValue(),
                Integer.valueOf(chunkPos.getY()).byteValue(),
                Integer.valueOf(chunkPos.getZ()).byteValue()
        };
    }

    private byte[] bytes(DataContainer data) throws IOException {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataFormats.JSON.writeTo(stream, data);
        final byte[] result = stream.toByteArray();
        if(logger.isDebugEnabled()){
            logger.debug("{} -> byte[] : {}",DataContainer.class.getSimpleName(),  new String(result));
        }
        return result;
    }

    private DataContainer asDataContainer(byte[] bytes) throws IOException {
        if(logger.isDebugEnabled()){
            logger.debug("byte[] -> {} : {}", DataContainer.class.getSimpleName(), new String(bytes));
        }
        return DataFormats.JSON.readFrom(new ByteArrayInputStream(bytes));
    }

}
