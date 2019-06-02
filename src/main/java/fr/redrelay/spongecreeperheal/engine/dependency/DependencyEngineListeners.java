package fr.redrelay.spongecreeperheal.engine.dependency;

import fr.redrelay.spongecreeperheal.engine.dependency.factory.RelativePositionDependencyFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.property.block.GravityAffectedProperty;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.util.Direction;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class DependencyEngineListeners {
    private static final DependencyEngineListeners INSTANCE = new DependencyEngineListeners();
    private DependencyEngineListeners(){}

    @Listener
    public void registerFallingBlocks(GamePostInitializationEvent e) {
        final RelativePositionDependencyFactory fallingBlockDependencyFactory = new RelativePositionDependencyFactory(Direction.DOWN);

        final Collection<BlockType> blocks = Sponge.getRegistry().getAllOf(BlockType.class);
        blocks.parallelStream()
                .filter(block -> {
                    final Optional<GravityAffectedProperty> opt = block.getProperty(GravityAffectedProperty.class);
                    return opt.isPresent() && opt.get().getValue();
                })
                .collect(Collectors.toList())
                .forEach(block -> {
                    DependencyEngine.getInstance().register(block, fallingBlockDependencyFactory);
                });

    }

    public static DependencyEngineListeners getInstance() { return INSTANCE; }
}
