package fr.redrelay.storage.world;

import org.spongepowered.api.plugin.PluginContainer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class WorldStorageManager {

    private static final WorldStorageManager INSTANCE = new WorldStorageManager();

    private final Map<String, WorldStorages> storages = new HashMap<>();

    private WorldStorageManager(){}

    public WorldStorages createWorldStorages(PluginContainer mod) {
        return createWorldStorages(mod, null);
    }

    public WorldStorages createWorldStorages(PluginContainer mod, String storageLabel) {
        final String key = storageLabel == null ? mod.getId() : mod.getId()+":"+storageLabel;
        if(storages.containsKey(key)) {
            throw new RuntimeException('"'+key+"\" "+WorldStorages.class.getSimpleName()+" already exists");
        }
        final WorldStorages instance = new WorldStorages(mod, key);
        storages.put(key, instance);
        return instance;
    }

    public Map<String, WorldStorages> getWorldStorages() {
        return Collections.unmodifiableMap(storages);
    }

    public static WorldStorageManager getInstance() { return INSTANCE; }
}
