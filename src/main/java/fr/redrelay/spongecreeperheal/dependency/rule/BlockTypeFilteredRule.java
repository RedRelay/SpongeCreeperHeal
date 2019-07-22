package fr.redrelay.spongecreeperheal.dependency.rule;

import fr.redrelay.spongecreeperheal.dependency.provider.DependencyProvider;
import fr.redrelay.spongecreeperheal.registry.DependencyRegistry;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;

import java.util.Collection;

public abstract class BlockTypeFilteredRule implements DependencyRule {

    @Override
    public void registerDependencies() {
        final Collection<BlockType> blocks = Sponge.getRegistry().getAllOf(BlockType.class);
        blocks.parallelStream()
                .filter(this::matches)
                .forEach(block -> DependencyRegistry.getInstance().register(block, getFactory(block)));
    }

    protected abstract boolean matches(BlockType block);
    protected abstract DependencyProvider getFactory(BlockType block);
}
