package fr.redrelay.spongecreeperheal.dependency.rule.impl;

import fr.redrelay.spongecreeperheal.dependency.factory.DependencyFactory;
import fr.redrelay.spongecreeperheal.dependency.factory.impl.VineDependencyFactory;
import fr.redrelay.spongecreeperheal.dependency.rule.BlockClassFilteredRule;
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
