package fr.redrelay.spongecreeperheal.dependency.rule.impl;

import fr.redrelay.spongecreeperheal.dependency.factory.DependencyProvider;
import fr.redrelay.spongecreeperheal.dependency.factory.impl.VineDependencyProvider;
import fr.redrelay.spongecreeperheal.dependency.rule.BlockClassFilteredRule;
import net.minecraft.block.BlockVine;
import org.spongepowered.api.block.BlockType;

public class VineDependencyRule extends BlockClassFilteredRule {

    public VineDependencyRule() {
        super(BlockVine.class);
    }

    @Override
    protected DependencyProvider getFactory(BlockType block) {
        return new VineDependencyProvider(this);
    }
}
