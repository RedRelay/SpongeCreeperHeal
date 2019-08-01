package fr.redrelay.storage.world;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.world.World;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class WorldStorages {

    private final PluginContainer plugin;
    private final String label;
    private final Logger logger;
    private final Map<UUID, WorldStorage> map = new HashMap<>();

    private final WorldEventListeners eventListeners;

    WorldStorages(PluginContainer plugin, String label) {
        this.plugin = plugin;
        this.label = label;
        this.logger = LoggerFactory.getLogger(WorldStorages.class.getSimpleName()+":"+label);
        this.eventListeners = new WorldEventListeners(this);
    }

    public Optional<WorldStorage> get(World world) {
        return get(world.getUniqueId());
    }

    public Optional<WorldStorage> get(UUID worldUUID) { return Optional.ofNullable(map.get(worldUUID));}

    public void registerEvents() {
        Sponge.getEventManager().registerListeners(plugin, eventListeners);
    }

    public void unregisterEvents() {
        Sponge.getEventManager().unregisterListeners(eventListeners);
    }

    void registerStorage(World world) throws IOException {
        final WorldStorage db = new WorldStorage(label, world);
        map.put(world.getUniqueId(), db);
    }

    void unregisterStorage(World world) {
        final WorldStorage storage = map.remove(world.getUniqueId());
        if(storage != null) {
            closeStorage(storage);
        }
    }

    public String getLabel() {
        return label;
    }

    private void closeStorage(WorldStorage storage) {
        try {
            storage.close();
        }catch(IOException e) {
            logger.error("Unable to close "+label+".db for world "+storage.getWorld()+", data may not be persisted correctly", e);
        }
    }
}
