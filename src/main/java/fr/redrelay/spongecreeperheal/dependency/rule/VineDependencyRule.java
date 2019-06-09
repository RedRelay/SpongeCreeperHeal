package fr.redrelay.spongecreeperheal.dependency.rule;

import fr.redrelay.spongecreeperheal.dependency.DependencyFactory;
import fr.redrelay.spongecreeperheal.dependency.factory.VineDependencyFactory;
import net.minecraft.block.BlockVine;
import org.spongepowered.api.block.BlockType;

public class VineDependencyRule extends BlockClassFilteredRule {

    public VineDependencyRule() {
        super(BlockVine.class);
    }

    @Override
    protected DependencyFactory getFactory(BlockType block) {
        return new VineDependencyFactory(this);
    }
}
