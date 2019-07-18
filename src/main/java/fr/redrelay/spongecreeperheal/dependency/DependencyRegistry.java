package fr.redrelay.spongecreeperheal.dependency;

import fr.redrelay.spongecreeperheal.SpongeCreeperHeal;
import fr.redrelay.spongecreeperheal.dependency.provider.DependencyProvider;
import fr.redrelay.spongecreeperheal.dependency.rule.DependencyRule;
import fr.redrelay.spongecreeperheal.healable.factory.Registry;
import org.spongepowered.api.block.BlockType;

public class DependencyRegistry extends Registry<BlockType, DependencyProvider> {

    public void register(DependencyRule rule) {
        SpongeCreeperHeal.getLogger().info("Register dependency rule \"{}\"",rule.getName());
        rule.registerDependencies();
    }

    @Override
    public void register(BlockType block, DependencyProvider factory) {
        SpongeCreeperHeal.getLogger().info("Register dependency provider \"{}\" for block \"{}\"",factory.getClass().getName(), block.getName());
        super.register(block, factory);
    }

}
