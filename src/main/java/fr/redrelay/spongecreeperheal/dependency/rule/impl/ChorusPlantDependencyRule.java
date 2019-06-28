package fr.redrelay.spongecreeperheal.dependency.rule.impl;

import fr.redrelay.spongecreeperheal.dependency.factory.DependencyFactory;
import fr.redrelay.spongecreeperheal.dependency.factory.impl.ChorusPlantDependencyFactory;
import fr.redrelay.spongecreeperheal.dependency.rule.BlockClassFilteredRule;
import net.minecraft.block.BlockChorusPlant;
import org.spongepowered.api.block.BlockType;

public class ChorusPlantDependencyRule extends BlockClassFilteredRule {

    public ChorusPlantDependencyRule() {
        super(BlockChorusPlant.class);
    }

    @Override
    protected DependencyFactory getFactory(BlockType block) {
        return new ChorusPlantDependencyFactory(this);
    }
}
