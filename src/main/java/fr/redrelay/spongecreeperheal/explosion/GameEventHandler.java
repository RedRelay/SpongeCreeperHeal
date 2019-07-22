package fr.redrelay.spongecreeperheal.explosion;

import fr.redrelay.spongecreeperheal.SpongeCreeperHeal;
import fr.redrelay.spongecreeperheal.adapter.MinecraftAdapter;
import fr.redrelay.spongecreeperheal.dependency.rule.impl.*;
import fr.redrelay.spongecreeperheal.factory.explosion.ExplosionSnapshotFactory;
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
        final ExplosionSnapshotFactory explosionSnapshotFactory = ExplosionSnapshotFactory.getInstance();
        explosionSnapshotFactory.register(new GravityAffectedDependencyRule());
        MinecraftAdapter.getInstance().getDownLayedBlocks().forEach(blockClass -> explosionSnapshotFactory.register(new DownLayedDependencyRule(blockClass)));
        MinecraftAdapter.getInstance().getOppositeFacingLayedBlocks().forEach(blockClass -> explosionSnapshotFactory.register(new DirectionalDependencyRule(blockClass, true)));
        MinecraftAdapter.getInstance().getFacingLayedBlocks().forEach(blockClass -> explosionSnapshotFactory.register(new DirectionalDependencyRule(blockClass)));
        explosionSnapshotFactory.register(new VineDependencyRule());
        explosionSnapshotFactory.register(new ChorusPlantDependencyRule());
        explosionSnapshotFactory.register(new ChorusFlowerDependencyRule());
        explosionSnapshotFactory.register(new FireDependencyRule());

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
