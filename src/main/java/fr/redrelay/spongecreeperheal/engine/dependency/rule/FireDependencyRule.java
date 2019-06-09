package fr.redrelay.spongecreeperheal.engine.dependency.rule;

import fr.redrelay.spongecreeperheal.engine.dependency.DependencyFactory;
import fr.redrelay.spongecreeperheal.engine.dependency.factory.FireDependencyFactory;
import net.minecraft.block.BlockFire;
import org.spongepowered.api.block.BlockType;

public class FireDependencyRule extends BlockClassFilteredRule {
    public FireDependencyRule() {
        super(BlockFire.class);
    }

    @Override
    protected DependencyFactory getFactory(BlockType block) {
        return new FireDependencyFactory(this);
    }
}
