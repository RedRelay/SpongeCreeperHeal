package fr.redrelay.storage;

import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.world.Chunk;

public class StorageKeyFactory {

    private static final StorageKeyFactory INSTANCE = new StorageKeyFactory();

    private StorageKeyFactory() {}

    public static StorageKeyFactory getInstance() { return INSTANCE; }

    public ChunkKey getChunkKey(Chunk chunk) {
        return new ChunkKey(chunk.getPosition());
    }

    public StringKey getRawKey(String str) {
        return new StringKey(str);
    }


    public interface Key {
        byte[] getQuery();
    }

    public class StringKey implements Key {

        private final String key;

        private StringKey(String query) {
            this.key = query;
        }


        @Override
        public byte[] getQuery() {
            return key.getBytes();
        }
    }

    public class ChunkKey implements Key {

        private final Vector3i chunk;

        private ChunkKey(Vector3i chunk) {
            this.chunk = chunk;
        }

        @Override
        public byte[] getQuery() {
            return new byte[]{
                    Integer.valueOf(chunk.getX()).byteValue(),
                    Integer.valueOf(chunk.getY()).byteValue(),
                    Integer.valueOf(chunk.getZ()).byteValue()
            };
        }
    }

}
