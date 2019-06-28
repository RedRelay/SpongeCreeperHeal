package fr.redrelay.spongecreeperheal.dependency.rule;

import fr.redrelay.spongecreeperheal.dependency.factory.DependencyFactory;
import fr.redrelay.spongecreeperheal.explosion.ExplosionSnapshotFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;

import java.util.Collection;

public abstract class BlockTypeFilteredRule implements DependencyRule {

    @Override
    public void registerDependencies() {
        final Collection<BlockType> blocks = Sponge.getRegistry().getAllOf(BlockType.class);
        blocks.parallelStream()
                .filter(this::matches)
                .forEach(block -> ExplosionSnapshotFactory.getInstance().register(block, getFactory(block)));
    }

    protected abstract boolean matches(BlockType block);
    protected abstract DependencyFactory getFactory(BlockType block);
}
