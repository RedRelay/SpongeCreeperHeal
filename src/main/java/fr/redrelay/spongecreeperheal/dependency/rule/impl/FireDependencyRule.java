package fr.redrelay.spongecreeperheal.dependency.rule.impl;

import fr.redrelay.spongecreeperheal.dependency.factory.DependencyFactory;
import fr.redrelay.spongecreeperheal.dependency.factory.impl.FireDependencyFactory;
import fr.redrelay.spongecreeperheal.dependency.rule.BlockClassFilteredRule;
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
