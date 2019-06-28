package fr.redrelay.spongecreeperheal.dependency.rule.impl;

import fr.redrelay.spongecreeperheal.dependency.factory.DependencyFactory;
import fr.redrelay.spongecreeperheal.dependency.factory.impl.DirectionalDependencyFactory;
import fr.redrelay.spongecreeperheal.dependency.rule.BlockClassFilteredRule;
import net.minecraft.block.Block;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.util.Direction;

public class DownLayedDependencyRule extends BlockClassFilteredRule {

    public DownLayedDependencyRule(Class<? extends Block> blockClass) {
        super(blockClass);
    }

    @Override
    protected DependencyFactory getFactory(BlockType block) {
        return new DirectionalDependencyFactory.Static(this, Direction.DOWN);
    }
}
