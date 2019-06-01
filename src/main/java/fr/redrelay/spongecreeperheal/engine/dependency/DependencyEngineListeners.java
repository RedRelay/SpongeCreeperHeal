package fr.redrelay.spongecreeperheal.engine.dependency;

import fr.redrelay.spongecreeperheal.engine.dependency.factory.RelativePositionDependencyFactory;
import net.minecraft.block.BlockFalling;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.util.Direction;

import java.util.Collection;
import java.util.stream.Collectors;

public class DependencyEngineListeners {
    private static final DependencyEngineListeners INSTANCE = new DependencyEngineListeners();
    private DependencyEngineListeners(){}

    /**
     * TODO : GravityAffected seems not to be implmented yet in Sponge, so we use an alternative using MNS. It should be replaced by Sponge API impl as soon as possible
     * @param e
     */
    @Listener
    public void registerFallingBlocks(GamePostInitializationEvent e) {
        final RelativePositionDependencyFactory fallingBlockDependencyFactory = new RelativePositionDependencyFactory(Direction.DOWN);

        final Collection<BlockType> blocks = Sponge.getRegistry().getAllOf(BlockType.class);
        blocks.parallelStream()
                .filter(block -> BlockFalling.class.isAssignableFrom(block.getClass()))
                .collect(Collectors.toList())
                .forEach(block -> {
                    DependencyEngine.getInstance().register(block, fallingBlockDependencyFactory);
                });

    }

    public static DependencyEngineListeners getInstance() { return INSTANCE; }
}
