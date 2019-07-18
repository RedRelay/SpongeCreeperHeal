package fr.redrelay.spongecreeperheal.dependency.rule.impl;

import fr.redrelay.spongecreeperheal.dependency.provider.DependencyProvider;
import fr.redrelay.spongecreeperheal.dependency.provider.impl.ChorusPlantDependencyProvider;
import fr.redrelay.spongecreeperheal.dependency.rule.BlockClassFilteredRule;
import net.minecraft.block.BlockChorusPlant;
import org.spongepowered.api.block.BlockType;

public class ChorusPlantDependencyRule extends BlockClassFilteredRule {

    public ChorusPlantDependencyRule() {
        super(BlockChorusPlant.class);
    }

    @Override
    protected DependencyProvider getFactory(BlockType block) {
        return new ChorusPlantDependencyProvider(this);
    }
}
