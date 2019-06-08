package fr.redrelay.spongecreeperheal.engine.dependency.rule;

import fr.redrelay.spongecreeperheal.engine.dependency.DependencyFactory;
import fr.redrelay.spongecreeperheal.engine.dependency.factory.ChorusFlowerDependencyFactory;
import net.minecraft.block.BlockChorusFlower;
import org.spongepowered.api.block.BlockType;

public class ChorusFlowerDependencyRule extends BlockClassFilteredRule {

    public ChorusFlowerDependencyRule() {
        super(BlockChorusFlower.class);
    }

    @Override
    protected DependencyFactory getFactory(BlockType block) {
        return new ChorusFlowerDependencyFactory(this);
    }
}
