package fr.redrelay.spongecreeperheal.dependency.rule;

import fr.redrelay.spongecreeperheal.dependency.DependencyFactory;
import fr.redrelay.spongecreeperheal.dependency.factory.ChorusPlantDependencyFactory;
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
