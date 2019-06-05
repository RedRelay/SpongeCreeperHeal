package fr.redrelay.spongecreeperheal.engine.dependency.rule;

import net.minecraft.block.Block;
import org.spongepowered.api.block.BlockType;

public abstract class BlockClassFilteredRule extends BlockTypeFilteredRule {

    protected final Class<? extends Block> blockClass;

    protected BlockClassFilteredRule(Class<? extends Block> blockClass) {
        this.blockClass = blockClass;
    }

    /*
    public BlockClassFilteredRule(Class<? extends Block> blockClass, DependencyFactory factory) {
        super();
        this.dependencyFactory = factory;
        this.blockClass = blockClass;
    }
     */

    @Override
    public String getName() {
        return super.getName()+"<"+this.blockClass.getName()+">";
    }

    @Override
    protected boolean matches(BlockType block) {
        return this.blockClass.isAssignableFrom(block.getClass());
    }
}
