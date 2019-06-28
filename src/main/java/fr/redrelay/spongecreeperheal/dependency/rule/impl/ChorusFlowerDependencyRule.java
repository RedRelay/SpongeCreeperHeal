package fr.redrelay.spongecreeperheal.dependency.rule.impl;

import fr.redrelay.spongecreeperheal.dependency.factory.DependencyFactory;
import fr.redrelay.spongecreeperheal.dependency.factory.impl.ChorusFlowerDependencyFactory;
import fr.redrelay.spongecreeperheal.dependency.rule.BlockClassFilteredRule;
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
