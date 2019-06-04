package fr.redrelay.spongecreeperheal;

import com.google.inject.Inject;
import fr.redrelay.spongecreeperheal.chunk.HealableChunk;
import fr.redrelay.spongecreeperheal.chunk.HealableChunksEventListeners;
import fr.redrelay.spongecreeperheal.chunk.component.HealableEntry;
import fr.redrelay.spongecreeperheal.chunk.component.HealableExplosion;
import fr.redrelay.spongecreeperheal.engine.dependency.DependencyEngineListeners;
import fr.redrelay.spongecreeperheal.handler.ExplosionHandler;
import fr.redrelay.spongecreeperheal.storage.world.WorldStoragesEventListeners;
import fr.redrelay.spongecreeperheal.task.HealTask;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.data.DataManager;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;

import java.nio.file.Path;
import java.util.Optional;

@Plugin(id = "spongecreeperheal", name = "Sponge Creeper Heal", version = "0.0")
public class SpongeCreeperHeal {

    private static SpongeCreeperHeal instance;

    public SpongeCreeperHeal() {
        instance = this;
    }

    @Inject
    private Logger logger;

    //@Inject
    @DefaultConfig(sharedRoot = true)
    private ConfigurationLoader configurationLoader;

    @Listener
    public void onGamePreInit(GamePreInitializationEvent e) {
        final DataManager dataManager = Sponge.getDataManager();

        dataManager.registerBuilder(HealableChunk.class, new HealableChunk.HealableChunkBuilder());
        dataManager.registerBuilder(HealableExplosion.class, new HealableExplosion.HealableExplosionBuilder());
        dataManager.registerBuilder(HealableEntry.class, new HealableEntry.HealableEntryBuilder());
    }

    @Listener
    public void onGameInit(GameInitializationEvent e) {
        final EventManager eventManager = Sponge.getEventManager();

        eventManager.registerListeners(this, DependencyEngineListeners.getInstance());

        eventManager.registerListeners(this, HealableChunksEventListeners.getInstance());
        eventManager.registerListeners(this, WorldStoragesEventListeners.getInstance());
        eventManager.registerListeners(this, HealTask.getInstance());
        eventManager.registerListeners(this, ExplosionHandler.getInstance());
    }



    public static Optional<SpongeCreeperHeal> getInstance() { return Optional.ofNullable(instance); }
    public static Logger getLogger() {
        final Optional<SpongeCreeperHeal> instance = getInstance();
        if(instance.isPresent()) {
            if(instance.get().logger != null) {
                return instance.get().logger;
            }
        }
        return LoggerFactory.getLogger("SpongeCreeperHeal");
    }

    public static Optional<ConfigurationLoader> getConfig() {
        final Optional<SpongeCreeperHeal> instance = getInstance();
        if(instance.isPresent()) {
            final Optional<ConfigurationLoader> config = Optional.ofNullable(instance.get().configurationLoader);
            if(!config.isPresent()) {
                instance.get().logger.error("Unable to load SpongeCreeperHeal config");
            }
            return config;
        }
        return Optional.empty();
    }

}
