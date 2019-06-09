package fr.redrelay.spongecreeperheal.engine.dependency.rule;

import fr.redrelay.spongecreeperheal.engine.dependency.DependencyFactory;
import fr.redrelay.spongecreeperheal.engine.dependency.factory.DirectionalDependencyFactory;
import net.minecraft.block.Block;
import org.spongepowered.api.block.BlockType;

public class DirectionalDependencyRule extends BlockClassFilteredRule{

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
