package fr.redrelay.spongecreeperheal.engine.dependency.rule;

import fr.redrelay.spongecreeperheal.engine.dependency.DependencyFactory;
import fr.redrelay.spongecreeperheal.engine.dependency.factory.FacingDependencyFactory;
import net.minecraft.block.Block;
import org.spongepowered.api.block.BlockType;

public class FacingDependencyRule extends BlockClassFilteredRule{

    final boolean isOpposite;

    public FacingDependencyRule(Class<? extends Block> blockClass, boolean isOpposite) {
        super(blockClass);
        this.isOpposite = isOpposite;
    }

    public FacingDependencyRule(Class<? extends Block> blockClass) {
        this(blockClass, false);
    }

    @Override
    protected DependencyFactory getFactory(BlockType block) {
        return new FacingDependencyFactory(this, this.isOpposite);
    }
}
