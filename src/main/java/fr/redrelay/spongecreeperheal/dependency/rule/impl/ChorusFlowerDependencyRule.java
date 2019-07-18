package fr.redrelay.spongecreeperheal.dependency.rule.impl;

import fr.redrelay.spongecreeperheal.dependency.factory.DependencyProvider;
import fr.redrelay.spongecreeperheal.dependency.factory.impl.ChorusFlowerDependencyProvider;
import fr.redrelay.spongecreeperheal.dependency.rule.BlockClassFilteredRule;
import net.minecraft.block.BlockChorusFlower;
import org.spongepowered.api.block.BlockType;

public class ChorusFlowerDependencyRule extends BlockClassFilteredRule {

    public ChorusFlowerDependencyRule() {
        super(BlockChorusFlower.class);
    }

    @Override
    protected DependencyProvider getFactory(BlockType block) {
        return new ChorusFlowerDependencyProvider(this);
    }
}
