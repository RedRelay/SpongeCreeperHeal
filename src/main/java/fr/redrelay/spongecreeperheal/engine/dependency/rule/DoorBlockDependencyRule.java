package fr.redrelay.spongecreeperheal.engine.dependency.rule;

import fr.redrelay.adapter.MinecraftAdapter;
import fr.redrelay.spongecreeperheal.engine.dependency.DependencyEngine;
import fr.redrelay.spongecreeperheal.engine.dependency.factory.SupportedByBottomDependencyFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;

import java.util.Collection;

public class DoorBlockDependencyRule implements DependencyRule {

    @Override
    public void registerDependencies() {
        final SupportedByBottomDependencyFactory supportedByBottomDependencyFactory = new SupportedByBottomDependencyFactory();

        final Collection<BlockType> blocks = Sponge.getRegistry().getAllOf(BlockType.class);
        blocks.parallelStream()
                .filter(block -> MinecraftAdapter.getInstance().isDoor(block))
                .forEach(block -> {
                    DependencyEngine.getInstance().register(block, supportedByBottomDependencyFactory);
                });
    }
}
