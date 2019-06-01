package fr.redrelay.spongecreeperheal.storage.world;

import org.spongepowered.api.world.World;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class WorldStorages {
    private static final WorldStorages INSTANCE = new WorldStorages();

    private Map<String, WorldStorage> map = new HashMap<>();

    private WorldStorages() {}

    public Optional<WorldStorage> get(String worldName) {
        return Optional.ofNullable(map.get(worldName));
    }

    protected void registerStorage(World world) throws IOException {
        final WorldStorage db = new WorldStorage(world);
        map.put(world.getName(), db);
    }

    protected void unregisterStorage(World world) {
        final WorldStorage db = map.remove(world.getName());
        if(db != null) {
            db.close();
        }
    }

    protected void clearStorages() {
        map.entrySet().forEach(entry -> entry.getValue().close());
        map.clear();
    }

    public static WorldStorages getInstance() { return INSTANCE; }
}
