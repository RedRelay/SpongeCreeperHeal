package fr.redrelay.spongecreeperheal.dependency.rule.impl;

import fr.redrelay.spongecreeperheal.dependency.provider.DependencyProvider;
import fr.redrelay.spongecreeperheal.dependency.provider.impl.DirectionalDependencyProvider;
import fr.redrelay.spongecreeperheal.dependency.rule.BlockClassFilteredRule;
import net.minecraft.block.Block;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.util.Direction;

public class DownLayedDependencyRule extends BlockClassFilteredRule {

    public DownLayedDependencyRule(Class<? extends Block> blockClass) {
        super(blockClass);
    }

    @Override
    protected DependencyProvider getFactory(BlockType block) {
        return new DirectionalDependencyProvider.Static(this, Direction.DOWN);
    }
}
