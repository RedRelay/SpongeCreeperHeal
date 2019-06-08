package fr.redrelay.spongecreeperheal.engine.dependency.rule;

import fr.redrelay.spongecreeperheal.engine.dependency.DependencyFactory;
import fr.redrelay.spongecreeperheal.engine.dependency.factory.ChorusPlantDependencyFactory;
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
