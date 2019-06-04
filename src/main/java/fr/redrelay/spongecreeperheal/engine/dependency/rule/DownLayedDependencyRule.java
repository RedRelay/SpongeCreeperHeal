package fr.redrelay.spongecreeperheal.engine.dependency.rule;

import fr.redrelay.spongecreeperheal.engine.dependency.factory.DownLayedDependencyFactory;
import net.minecraft.block.Block;
import org.spongepowered.api.block.BlockType;

public class DownLayedDependencyRule extends BlockTypeFilteredRule {

    private final Class<? extends Block> blockClass;

    public DownLayedDependencyRule(Class<? extends Block> blockClass) {
        super();
        this.dependencyFactory = new DownLayedDependencyFactory(this);
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
}
