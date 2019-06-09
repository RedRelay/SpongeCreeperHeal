package fr.redrelay.spongecreeperheal.dependency.rule;

import net.minecraft.block.Block;
import org.spongepowered.api.block.BlockType;

public abstract class BlockClassFilteredRule extends BlockTypeFilteredRule {

    private final Class<? extends Block> blockClass;

    public BlockClassFilteredRule(Class<? extends Block> blockClass) {
        this.blockClass = blockClass;
    }

    @Override
    public String getName() {
        return super.getName()+"<"+this.blockClass.getName()+">";
    }

    @Override
    protected boolean matches(BlockType block) {
        return this.blockClass.isAssignableFrom(block.getClass());
    }

    public Class<? extends Block> getBlockClass() {
        return blockClass;
    }
}
