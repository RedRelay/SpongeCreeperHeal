package fr.redrelay.spongecreeperheal.engine.dependency;

import fr.redrelay.adapter.MinecraftAdapter;
import fr.redrelay.spongecreeperheal.engine.dependency.rule.DownLayedDependencyRule;
import fr.redrelay.spongecreeperheal.engine.dependency.rule.GravityAffectedDependencyRule;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;

public class DependencyEngineListeners {
    private static final DependencyEngineListeners INSTANCE = new DependencyEngineListeners();
    private DependencyEngineListeners(){}

    @Listener
    public void registerDependencyBlocks(GamePostInitializationEvent e) {
        final DependencyEngine dependencyEngine = DependencyEngine.getInstance();
        dependencyEngine.register(new GravityAffectedDependencyRule());
        MinecraftAdapter.getInstance().getDownLayedBlocks().forEach(blockClass -> dependencyEngine.register(new DownLayedDependencyRule(blockClass)));
    }

    public static DependencyEngineListeners getInstance() { return INSTANCE; }
}
