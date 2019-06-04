package fr.redrelay.spongecreeperheal.engine.dependency;

import fr.redrelay.adapter.MinecraftAdapter;
import fr.redrelay.spongecreeperheal.SpongeCreeperHeal;
import fr.redrelay.spongecreeperheal.engine.dependency.rule.DownLayedDependencyRule;
import fr.redrelay.spongecreeperheal.engine.dependency.rule.GravityAffectedDependencyRule;
import fr.redrelay.spongecreeperheal.tool.tracker.dependency.DependencyTracker;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class DependencyEngineListeners {
    private static final DependencyEngineListeners INSTANCE = new DependencyEngineListeners();
    private DependencyEngineListeners(){}

    @Listener
    public void registerDependencyBlocks(GamePostInitializationEvent e) {
        final DependencyEngine dependencyEngine = DependencyEngine.getInstance();
        dependencyEngine.register(new GravityAffectedDependencyRule());
        MinecraftAdapter.getInstance().getDownLayedBlocks().forEach(blockClass -> dependencyEngine.register(new DownLayedDependencyRule(blockClass)));

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

    public static DependencyEngineListeners getInstance() { return INSTANCE; }
}
