package fr.redrelay.spongecreeperheal.engine.dependency.rule;

import fr.redrelay.spongecreeperheal.engine.dependency.DependencyEngine;
import fr.redrelay.spongecreeperheal.engine.dependency.DependencyFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;

import java.util.Collection;

public abstract class BlockTypeFilteredRule implements DependencyRule {

    @Override
    public void registerDependencies() {
        final Collection<BlockType> blocks = Sponge.getRegistry().getAllOf(BlockType.class);
        blocks.parallelStream()
                .filter(this::matches)
                .forEach(block -> DependencyEngine.getInstance().register(block, getFactory(block)));
    }

    protected abstract boolean matches(BlockType block);
    protected abstract DependencyFactory getFactory(BlockType block);
}
