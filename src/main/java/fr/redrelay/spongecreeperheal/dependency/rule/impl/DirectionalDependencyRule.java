package fr.redrelay.spongecreeperheal.dependency.rule.impl;

import fr.redrelay.spongecreeperheal.dependency.factory.DependencyFactory;
import fr.redrelay.spongecreeperheal.dependency.factory.impl.DirectionalDependencyFactory;
import fr.redrelay.spongecreeperheal.dependency.rule.BlockClassFilteredRule;
import net.minecraft.block.Block;
import org.spongepowered.api.block.BlockType;

public class DirectionalDependencyRule extends BlockClassFilteredRule {

    final boolean isOpposite;

    public DirectionalDependencyRule(Class<? extends Block> blockClass, boolean isOpposite) {
        super(blockClass);
        this.isOpposite = isOpposite;
    }

    public DirectionalDependencyRule(Class<? extends Block> blockClass) {
        this(blockClass, false);
    }

    @Override
    protected DependencyFactory getFactory(BlockType block) {
        return new DirectionalDependencyFactory(this, this.isOpposite);
    }
}
