package fr.redrelay.spongecreeperheal.engine.dependency.rule;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.spongecreeperheal.engine.dependency.DependencyEngine;
import fr.redrelay.spongecreeperheal.engine.dependency.DependencyFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;

import java.util.Collection;
import java.util.Map;

public abstract class BlockTypeFilteredRule implements DependencyRule {

    protected DependencyFactory dependencyFactory;

    protected BlockTypeFilteredRule() {}

    public BlockTypeFilteredRule(DependencyFactory<BlockSnapshot, Vector3i, Map<Vector3i, BlockState>> dependencyFactory) {
        this.dependencyFactory = dependencyFactory;
    }

    @Override
    public void registerDependencies() {
        final Collection<BlockType> blocks = Sponge.getRegistry().getAllOf(BlockType.class);
        blocks.parallelStream()
                .filter(this::matches)
                .forEach(block -> {
                    DependencyEngine.getInstance().register(block, dependencyFactory);
                });
    }

    protected abstract boolean matches(BlockType block);
}
