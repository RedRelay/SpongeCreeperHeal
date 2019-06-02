package fr.redrelay.spongecreeperheal.engine.dependency;

import fr.redrelay.spongecreeperheal.engine.dependency.rule.DoorBlockDependencyRule;
import fr.redrelay.spongecreeperheal.engine.dependency.rule.FallingBlockDependencyRule;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;

public class DependencyEngineListeners {
    private static final DependencyEngineListeners INSTANCE = new DependencyEngineListeners();
    private DependencyEngineListeners(){}

    @Listener
    public void registerDependencyBlocks(GamePostInitializationEvent e) {
        new FallingBlockDependencyRule().registerDependencies();
        new DoorBlockDependencyRule().registerDependencies();
    }

    public static DependencyEngineListeners getInstance() { return INSTANCE; }
}
