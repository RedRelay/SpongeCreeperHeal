package fr.redrelay.spongecreeperheal.explosion;

import fr.redrelay.spongecreeperheal.SpongeCreeperHeal;
import fr.redrelay.spongecreeperheal.adapter.MinecraftAdapter;
import fr.redrelay.spongecreeperheal.dependency.rule.impl.*;
import fr.redrelay.spongecreeperheal.registry.DependencyRegistry;
import fr.redrelay.spongecreeperheal.tool.tracker.dependency.DependencyTracker;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * All event related to Derpendency
 */
public class GameEventHandler {
    private static final GameEventHandler INSTANCE = new GameEventHandler();
    private GameEventHandler(){}

    /**
     * Registers all rules for block
     * Enable tracker
     * @param e
     */
    @Listener
    public void registerDependencyBlocks(GamePostInitializationEvent e) {
        final DependencyRegistry dependencyRegistry = DependencyRegistry.getInstance();
        dependencyRegistry.register(new GravityAffectedDependencyRule());
        MinecraftAdapter.getInstance().getDownLayedBlocks().forEach(blockClass -> dependencyRegistry.register(new DownLayedDependencyRule(blockClass)));
        MinecraftAdapter.getInstance().getOppositeFacingLayedBlocks().forEach(blockClass -> dependencyRegistry.register(new DirectionalDependencyRule(blockClass, true)));
        MinecraftAdapter.getInstance().getFacingLayedBlocks().forEach(blockClass -> dependencyRegistry.register(new DirectionalDependencyRule(blockClass)));
        dependencyRegistry.register(new VineDependencyRule());
        dependencyRegistry.register(new ChorusPlantDependencyRule());
        dependencyRegistry.register(new ChorusFlowerDependencyRule());
        dependencyRegistry.register(new FireDependencyRule());

        SpongeCreeperHeal.getConfig().ifPresent(config -> {
            Optional<String> optFilePath = Optional.empty();
            try {
                optFilePath = Optional.ofNullable(config.load().getNode("tool", "tracker", "dependency", "path").getString());
            }catch(IOException error) {
                SpongeCreeperHeal.getLogger().error("Unable to write dependency tracker : {}", error.getMessage());
            }
            optFilePath.ifPresent(filePath -> {
                final File file = new File(filePath);
                try {
                    new DependencyTracker().createMarkdown(file);
                    SpongeCreeperHeal.getLogger().info("DependencyTracker generated at {}", file.getAbsolutePath());
                }catch(IOException error) {
                    SpongeCreeperHeal.getLogger().error("Unable to write dependency tracker at {} : {}", file.getAbsolutePath(), error.getMessage());
                }
            });
        });

    }

    public static GameEventHandler getInstance() { return INSTANCE; }
}
