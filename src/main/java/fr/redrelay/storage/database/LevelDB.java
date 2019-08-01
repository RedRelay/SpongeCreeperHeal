package fr.redrelay.storage.database;

import fr.redrelay.storage.StorageKeyFactory;
import org.fusesource.leveldbjni.JniDBFactory;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.persistence.DataFormats;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * A LevelDB wrapper to store DataContainer only
 * @see DataContainer
 */
public class LevelDB {

    private final DB db;

    public LevelDB(File dbFile) throws IOException {
        Options options = new Options();
        options.createIfMissing(true);
        this.db = JniDBFactory.factory.open(dbFile, options);
    }

    public void close() throws IOException {
        db.close();
    }

    public void save(StorageKeyFactory.Key key, DataContainer data) throws IOException {
        db.put(key.getQuery(), bytes(data));
    }

    public Optional<DataContainer> get(StorageKeyFactory.Key key) throws IOException {
        final byte[] data = db.get(key.getQuery());
        if(data != null) {
            return Optional.of(asDataContainer(data));
        }
        return Optional.empty();
    }

    public void delete(StorageKeyFactory.Key key) {
        db.delete(key.getQuery());
    }

    private byte[] bytes(DataContainer data) throws IOException {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataFormats.JSON.writeTo(stream, data);
        return stream.toByteArray();
    }

    private DataContainer asDataContainer(byte[] bytes) throws IOException {
        return DataFormats.JSON.readFrom(new ByteArrayInputStream(bytes));
    }

}
