package fr.redrelay.spongecreeperheal.dependency.rule;

import fr.redrelay.spongecreeperheal.dependency.DependencyFactory;
import fr.redrelay.spongecreeperheal.dependency.factory.ChorusFlowerDependencyFactory;
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
