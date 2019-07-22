package fr.redrelay.spongecreeperheal.registry;

import fr.redrelay.spongecreeperheal.SpongeCreeperHeal;
import fr.redrelay.spongecreeperheal.dependency.provider.DependencyProvider;
import fr.redrelay.spongecreeperheal.dependency.rule.DependencyRule;
import org.spongepowered.api.block.BlockType;

public class DependencyRegistry extends Registry<BlockType, DependencyProvider> {

    private final static DependencyRegistry INSTANCE = new DependencyRegistry();

    private DependencyRegistry() {};

    public void register(DependencyRule rule) {
        SpongeCreeperHeal.getLogger().info("Register dependency rule \"{}\"",rule.getName());
        rule.registerDependencies();
    }

    @Override
    public void register(BlockType block, DependencyProvider factory) {
        SpongeCreeperHeal.getLogger().info("Register dependency provider \"{}\" for block \"{}\"",factory.getClass().getName(), block.getName());
        super.register(block, factory);
    }

    public static DependencyRegistry getInstance() {
        return INSTANCE;
    }
}
