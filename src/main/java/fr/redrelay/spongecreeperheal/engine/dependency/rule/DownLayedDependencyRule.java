package fr.redrelay.spongecreeperheal.engine.dependency.rule;

import fr.redrelay.spongecreeperheal.engine.dependency.DependencyFactory;
import fr.redrelay.spongecreeperheal.engine.dependency.factory.DirectionalDependencyFactory;
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
