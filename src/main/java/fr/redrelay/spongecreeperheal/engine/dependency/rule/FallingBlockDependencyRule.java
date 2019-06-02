package fr.redrelay.spongecreeperheal.engine.dependency.rule;

import fr.redrelay.spongecreeperheal.engine.dependency.DependencyEngine;
import fr.redrelay.spongecreeperheal.engine.dependency.factory.SupportedByBottomDependencyFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.property.block.GravityAffectedProperty;

import java.util.Collection;
import java.util.Optional;

public class FallingBlockDependencyRule implements DependencyRule {

    @Override
    public void registerDependencies() {
        final SupportedByBottomDependencyFactory supportedByBottomDependencyFactory = new SupportedByBottomDependencyFactory();

        final Collection<BlockType> blocks = Sponge.getRegistry().getAllOf(BlockType.class);
        blocks.parallelStream()
                .filter(block -> {
                    final Optional<GravityAffectedProperty> opt = block.getProperty(GravityAffectedProperty.class);
                    return opt.isPresent() && opt.get().getValue();
                })
                .forEach(block -> {
                    DependencyEngine.getInstance().register(block, supportedByBottomDependencyFactory);
                });
    }
}
